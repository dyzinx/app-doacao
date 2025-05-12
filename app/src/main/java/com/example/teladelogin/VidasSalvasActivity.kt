package com.example.teladelogin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class VidasSalvasActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var quantidadeVidas: TextView
    private lateinit var textoInfoEstado: TextView
    private lateinit var iconeMenu: ImageView
    private lateinit var iconeUsuario: ImageView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vidas_salvas)

        webView = findViewById(R.id.webViewMapa)
        quantidadeVidas = findViewById(R.id.quantidadeVidas)
        textoInfoEstado = findViewById(R.id.textoInfoEstado)
        iconeMenu = findViewById(R.id.iconeMenu)
        iconeUsuario = findViewById(R.id.iconeUsuario)

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.addJavascriptInterface(WebAppInterface(this), "Android")
        webView.loadUrl("file:///android_asset/mapa.html")

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
                    item.icon = ContextCompat.getDrawable(this@VidasSalvasActivity, getIconForMenuItem(item.itemId))
                    item.icon?.setTint(ContextCompat.getColor(this@VidasSalvasActivity, android.R.color.black))
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
            R.id.menu_historico -> startActivity(Intent(this, HistoricoDoacoesActivity::class.java))
            R.id.menu_vidas -> Toast.makeText(this, "Você já está em Vidas Salvas", Toast.LENGTH_SHORT).show()
            R.id.menu_config -> startActivity(Intent(this, ConfiguracoesActivity::class.java))
            R.id.menu_sobre -> startActivity(Intent(this, SobreActivity::class.java))
            R.id.menu_editar -> startActivity(Intent(this, EditarPerfilActivity::class.java))
            R.id.menu_sair -> finishAffinity()
            else -> Toast.makeText(this, "Ação desconhecida", Toast.LENGTH_SHORT).show()
        }
    }

    inner class WebAppInterface(private val context: Context) {
        @JavascriptInterface
        fun onStateClick(stateId: String) {
            runOnUiThread {
                val (vidas, mensagem) = getInfoByState(stateId)
                quantidadeVidas.text = vidas
                textoInfoEstado.text = mensagem
                textoInfoEstado.visibility = View.VISIBLE
            }
        }

        private fun getInfoByState(stateId: String): Pair<String, String> {
            return when (stateId) {
                "SP" -> "15.000" to "Em São Paulo, cerca de 15.000 vidas foram salvas graças à solidariedade de doadores."
                "RJ" -> "10.000" to "No Rio de Janeiro, cerca de 10.000 vidas foram salvas."
                "MG" -> "12.000" to "Em Minas Gerais, mais de 12.000 vidas foram salvas."
                "BA" -> "8.000" to "Na Bahia, cerca de 8.000 vidas foram salvas."
                "PR" -> "7.500" to "No Paraná, cerca de 7.500 vidas foram salvas."
                else -> "0" to "Estado $stateId clicado, sem dados disponíveis."
            }
        }
    }
}