package com.example.teladelogin

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.*

class CalendarioActivity : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var btnEscolherDia: Button
    private lateinit var iconeMenu: ImageView
    private lateinit var iconeUsuario: ImageView

    private var dataSelecionada: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario)

        val nomeHospital = intent.getStringExtra("nomeHospital")
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)
        val imagemUrl = intent.getStringExtra("imagemUrl")

        calendarView = findViewById(R.id.calendarView)
        btnEscolherDia = findViewById(R.id.btnEscolherDia)
        iconeMenu = findViewById(R.id.iconeMenu)
        iconeUsuario = findViewById(R.id.iconeUsuario)

        dataSelecionada = calendarView.date

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendario = Calendar.getInstance()
            calendario.set(year, month, dayOfMonth)
            dataSelecionada = calendario.timeInMillis
        }

        btnEscolherDia.setOnClickListener {
            val dataFormatada = android.text.format.DateFormat.format("dd/MM/yyyy", dataSelecionada).toString()
            val horarioSelecionado = "09:00" // Você pode trocar por um TimePicker futuramente

            // Obter endereço real
            val geocoder = Geocoder(this, Locale.getDefault())
            val enderecoCompleto = try {
                val resultados = geocoder.getFromLocation(latitude, longitude, 1)
                if (!resultados.isNullOrEmpty()) {
                    resultados[0].getAddressLine(0)
                } else {
                    "Endereço não disponível"
                }
            } catch (e: Exception) {
                "Endereço não disponível"
            }

            // Enviar dados para tela de confirmação
            val intent = Intent(this, ConfirmarAgendamentoActivity::class.java).apply {
                putExtra("nomeHospital", nomeHospital)
                putExtra("enderecoHospital", enderecoCompleto)
                putExtra("dataSelecionada", dataFormatada)
                putExtra("horarioSelecionado", horarioSelecionado)
                putExtra("imagemUrl", imagemUrl)
            }

            startActivity(intent)
        }

        iconeMenu.setOnClickListener { view ->
            showStyledMenuPopup(view, R.menu.menu_main)
        }

        iconeUsuario.setOnClickListener { view ->
            showStyledMenuPopup(view, R.menu.menu_profile)
        }
    }

    private fun showStyledMenuPopup(anchorView: View, menuRes: Int) {
        val wrapper = ContextThemeWrapper(this, R.style.PopupMenuStyle)

        val popup = PopupMenu(wrapper, anchorView).apply {
            menuInflater.inflate(menuRes, menu)

            for (i in 0 until menu.size()) {
                menu.getItem(i)?.let { item ->
                    item.icon = ContextCompat.getDrawable(this@CalendarioActivity, getIconForMenuItem(item.itemId))
                    item.icon?.setTint(ContextCompat.getColor(this@CalendarioActivity, android.R.color.black))
                }
            }

            setOnMenuItemClickListener { item ->
                handleMenuItemClick(item.itemId)
                true
            }

            forceShowIcons()
            show()
        }
    }

    private fun getIconForMenuItem(itemId: Int): Int {
        return when (itemId) {
            R.id.menu_historico -> R.drawable.ic_histor
            R.id.menu_vidas -> R.drawable.ic_life
            R.id.menu_config -> R.drawable.ic_settings
            R.id.menu_sobre -> R.drawable.ic_about
            R.id.menu_editar -> R.drawable.ic_edit
            R.id.menu_sair -> R.drawable.logout
            else -> 0
        }
    }

    private fun handleMenuItemClick(itemId: Int) {
        when (itemId) {
            R.id.menu_historico -> showToast("Histórico de doações (em construção)")
            R.id.menu_vidas -> showToast("Vidas salvas (em construção)")
            R.id.menu_config -> showToast("Configurações (em construção)")
            R.id.menu_sobre -> showToast("Sobre o app (em construção)")
            R.id.menu_editar -> showToast("Editar Perfil (em construção)")
            R.id.menu_sair -> finish()
        }
    }

    private fun PopupMenu.forceShowIcons() {
        try {
            val field = javaClass.getDeclaredField("mPopup")
            field.isAccessible = true
            val menuPopupHelper = field.get(this)
            menuPopupHelper.javaClass
                .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(menuPopupHelper, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
