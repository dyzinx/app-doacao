package com.example.teladelogin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class SelecaoHospitalActivity : AppCompatActivity(), LifecycleOwner {

    private lateinit var map: MapView
    private lateinit var edtSearch: EditText
    private lateinit var btnSearch: ImageView
    private lateinit var hospitalsList: LinearLayout
    private lateinit var geocoder: Geocoder

    private val REQUEST_LOCATION_PERMISSION = 1
    private var localizacaoAtual: GeoPoint? = null
    private var marcadorUsuario: Marker? = null
    private val marcadoresHospitais = mutableListOf<Marker>()

    private var dadosQuestionario: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(applicationContext, getSharedPreferences("osmdroid", MODE_PRIVATE))
        setContentView(R.layout.activity_selecao_hospital)

        edtSearch = findViewById(R.id.edtSearchLocation)
        btnSearch = findViewById(R.id.btnSearch)
        map = findViewById(R.id.map)
        hospitalsList = findViewById(R.id.hospitalsList)

        map.setMultiTouchControls(true)
        map.controller.setZoom(5.5)
        map.controller.setCenter(GeoPoint(-14.2350, -51.9253)) // Centro do Brasil

        geocoder = Geocoder(this, Locale.getDefault())
        dadosQuestionario = intent.extras

        btnSearch.setOnClickListener {
            val endereco = edtSearch.text.toString()
            if (endereco.isNotEmpty()) {
                buscarEndereco(endereco)
            }
        }

        val emModoTeste = intent.getBooleanExtra("modoTeste", false)
        if (!emModoTeste) {
            verificarPermissaoLocalizacao()
        } else {
            val pontoTeste = GeoPoint(-23.5505, -46.6333)
            localizacaoAtual = pontoTeste
            map.controller.setZoom(16.0)
            map.controller.setCenter(pontoTeste)
            mostrarMarcadorUsuario(pontoTeste)
            buscarHemocentrosProximos(pontoTeste)
        }
    }

    private fun verificarPermissaoLocalizacao() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        } else {
            obterLocalizacaoAtual()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            obterLocalizacaoAtual()
        } else {
            Toast.makeText(this, "Permissão de localização negada", Toast.LENGTH_LONG).show()
        }
    }

    private fun obterLocalizacaoAtual() {
        val locationManager = getSystemService(LOCATION_SERVICE) as android.location.LocationManager
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val location: Location? = locationManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER)
                ?: locationManager.getLastKnownLocation(android.location.LocationManager.NETWORK_PROVIDER)

            location?.let {
                localizacaoAtual = GeoPoint(it.latitude, it.longitude)
                map.controller.setZoom(16.0)
                map.controller.setCenter(localizacaoAtual)

                mostrarMarcadorUsuario(localizacaoAtual!!)
                buscarHemocentrosProximos(localizacaoAtual!!)
            } ?: run {
                Toast.makeText(this, "Localização indisponível", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun buscarEndereco(endereco: String) {
        try {
            val resultados = geocoder.getFromLocationName(endereco, 1)
            if (!resultados.isNullOrEmpty()) {
                val local = resultados[0]
                val geoPoint = GeoPoint(local.latitude, local.longitude)
                localizacaoAtual = geoPoint
                map.controller.setZoom(16.0)
                map.controller.setCenter(geoPoint)
                mostrarMarcadorUsuario(geoPoint)
                buscarHemocentrosProximos(geoPoint)
            } else {
                Toast.makeText(this, "Endereço não encontrado", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Erro ao buscar endereço: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarMarcadorUsuario(local: GeoPoint) {
        marcadorUsuario?.let { map.overlays.remove(it) }

        marcadorUsuario = Marker(map).apply {
            position = local
            title = "Você está aqui"
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            icon = ContextCompat.getDrawable(this@SelecaoHospitalActivity, R.drawable.ic_vc_esta_aqui)
        }

        map.overlays.add(marcadorUsuario)
        map.invalidate()
    }

    private fun buscarHemocentrosProximos(localizacao: GeoPoint) {
        lifecycleScope.launch {
            try {
                val lat = localizacao.latitude
                val lon = localizacao.longitude
                val radius = 50000

                val query = """
                [out:json];
                (
                  node["amenity"="blood_donation"](around:$radius,$lat,$lon);
                  way["amenity"="blood_donation"](around:$radius,$lat,$lon);
                  relation["amenity"="blood_donation"](around:$radius,$lat,$lon);
                  node["amenity"="blood_bank"](around:$radius,$lat,$lon);
                  way["amenity"="blood_bank"](around:$radius,$lat,$lon);
                  relation["amenity"="blood_bank"](around:$radius,$lat,$lon);
                  node["healthcare"="blood_donation"](around:$radius,$lat,$lon);
                  way["healthcare"="blood_donation"](around:$radius,$lat,$lon);
                  relation["healthcare"="blood_donation"](around:$radius,$lat,$lon);
                );
                out center;
            """.trimIndent()

                val response = withContext(Dispatchers.IO) {
                    val url = URL("https://overpass-api.de/api/interpreter")
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "POST"
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                    connection.doOutput = true
                    connection.outputStream.write("data=$query".toByteArray())
                    connection.inputStream.bufferedReader().use { it.readText() }
                }

                val json = JSONObject(response)
                val elements = json.getJSONArray("elements")

                hospitalsList.removeAllViews()
                marcadoresHospitais.forEach { map.overlays.remove(it) }
                marcadoresHospitais.clear()

                for (i in 0 until elements.length()) {
                    val obj = elements.getJSONObject(i)
                    val latPonto = obj.optDouble("lat", obj.optJSONObject("center")?.optDouble("lat") ?: continue)
                    val lonPonto = obj.optDouble("lon", obj.optJSONObject("center")?.optDouble("lon") ?: continue)
                    val nome = obj.optJSONObject("tags")?.optString("name") ?: "Posto de Doação"
                    val ponto = GeoPoint(latPonto, lonPonto)
                    val distancia = calcularDistanciaEmKm(localizacao, ponto)

                    val marcador = Marker(map).apply {
                        position = ponto
                        title = nome
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        setOnMarkerClickListener { marker, _ ->
                            val intent = Intent(this@SelecaoHospitalActivity, CalendarioActivity::class.java).apply {
                                putExtra("nomeHospital", marker.title)
                                putExtra("latitude", marker.position.latitude)
                                putExtra("longitude", marker.position.longitude)
                                putExtra("imagemUrl", "")
                                dadosQuestionario?.keySet()?.forEach { chave ->
                                    putExtra(chave, dadosQuestionario!!.getString(chave))
                                }
                            }
                            startActivity(intent)
                            true
                        }
                    }

                    map.overlays.add(marcador)
                    marcadoresHospitais.add(marcador)

                    val hospitalItem = layoutInflater.inflate(R.layout.item_hospital, null)
                    hospitalItem.findViewById<TextView>(R.id.nomeHospital).text = "$nome (${distancia.toInt()} km)"
                    hospitalItem.setOnClickListener {
                        map.controller.setZoom(17.0)
                        map.controller.animateTo(ponto)
                        marcador.showInfoWindow()
                    }
                    hospitalsList.addView(hospitalItem)
                }

                map.invalidate()

                if (elements.length() == 0) {
                    Toast.makeText(this@SelecaoHospitalActivity, "Nenhum posto de doação encontrado na região.", Toast.LENGTH_LONG).show()
                }

            } catch (e: Exception) {
                Toast.makeText(this@SelecaoHospitalActivity, "Erro ao buscar locais: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun calcularDistanciaEmKm(p1: GeoPoint, p2: GeoPoint): Double {
        val results = FloatArray(1)
        Location.distanceBetween(p1.latitude, p1.longitude, p2.latitude, p2.longitude, results)
        return results[0] / 1000.0
    }
}
