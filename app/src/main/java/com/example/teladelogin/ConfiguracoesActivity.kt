package com.example.teladelogin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class ConfiguracoesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracoes)

        val iconeMenu: ImageView = findViewById(R.id.iconeMenu)
        val iconeUsuario: ImageView = findViewById(R.id.iconeUsuario)

        iconeMenu.setOnClickListener { view -> showStyledMenuPopup(view, R.menu.menu_main) }
        iconeUsuario.setOnClickListener { view -> showStyledMenuPopup(view, R.menu.menu_profile) }

        val switchNotificacoes = findViewById<Switch>(R.id.switchNotificacoes)
        val switchModoEscuro = findViewById<Switch>(R.id.switchModoEscuro)
        val btnLimparCache = findViewById<Button>(R.id.btnLimparCache)
        val btnRedefinirPreferencias = findViewById<Button>(R.id.btnRedefinirPreferencias)
        val btnPoliticaPrivacidade = findViewById<Button>(R.id.btnPoliticaPrivacidade)

        switchNotificacoes.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this,
                if (isChecked) "Notificações ativadas" else "Notificações desativadas",
                Toast.LENGTH_SHORT).show()
        }

        switchModoEscuro.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this,
                if (isChecked) "Modo escuro ativado (ainda não funcional)" else "Modo claro ativado",
                Toast.LENGTH_SHORT).show()
        }

        btnLimparCache.setOnClickListener {
            Toast.makeText(this, "Cache limpo com sucesso!", Toast.LENGTH_SHORT).show()
        }

        btnRedefinirPreferencias.setOnClickListener {
            Toast.makeText(this, "Preferências redefinidas!", Toast.LENGTH_SHORT).show()
        }

        btnPoliticaPrivacidade.setOnClickListener {
            Toast.makeText(this, "Política de privacidade (futura tela)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showStyledMenuPopup(anchorView: View, menuRes: Int) {
        val popup = PopupMenu(this, anchorView)
        popup.menuInflater.inflate(menuRes, popup.menu)

        for (i in 0 until popup.menu.size()) {
            val item = popup.menu.getItem(i)
            item.icon = ContextCompat.getDrawable(this, getIconForMenuItem(item.itemId))
            item.icon?.setTint(ContextCompat.getColor(this, android.R.color.black))
        }

        popup.setOnMenuItemClickListener { item ->
            handleMenuItemClick(item.itemId)
            true
        }

        try {
            val field = popup.javaClass.getDeclaredField("mPopup")
            field.isAccessible = true
            val menuPopupHelper = field.get(popup)
            val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
            val method = classPopupHelper.getMethod("setForceShowIcon", Boolean::class.java)
            method.invoke(menuPopupHelper, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        popup.show()
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
            R.id.menu_vidas -> startActivity(Intent(this, VidasSalvasActivity::class.java))
            R.id.menu_config -> Toast.makeText(this, "Você já está em Configurações", Toast.LENGTH_SHORT).show()
            R.id.menu_sobre -> startActivity(Intent(this, SobreActivity::class.java))
            R.id.menu_editar -> startActivity(Intent(this, EditarPerfilActivity::class.java))
            R.id.menu_sair -> finish()
        }
    }
}
