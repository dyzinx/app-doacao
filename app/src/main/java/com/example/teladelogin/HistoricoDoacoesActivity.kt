package com.example.teladelogin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class HistoricoDoacoesActivity : AppCompatActivity() {

    private lateinit var iconeMenu: ImageView
    private lateinit var iconeUsuario: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historico_doacoes)

        iconeMenu = findViewById(R.id.iconeMenu)
        iconeUsuario = findViewById(R.id.iconeUsuario)

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
                    item.icon?.setTint(ContextCompat.getColor(this@HistoricoDoacoesActivity, android.R.color.black))
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
            val fields = this.javaClass.declaredFields
            for (field in fields) {
                if ("mPopup" == field.name) {
                    field.isAccessible = true
                    val menuPopupHelper = field.get(this)
                    val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                    val setForceIcons = classPopupHelper.getMethod("setForceShowIcon", Boolean::class.java)
                    setForceIcons.invoke(menuPopupHelper, true)
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleMenuItemClick(itemId: Int) {
        when (itemId) {
            R.id.menu_historico -> {
                val intent = Intent(this, HistoricoDoacoesActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_vidas -> {
                val intent = Intent(this, VidasSalvasActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_config -> {
                val intent = Intent(this, ConfiguracoesActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_sobre -> {
                val intent = Intent(this, SobreActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_editar -> {
                val intent = Intent(this, EditarPerfilActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_sair -> {
                finishAffinity() // Fecha todas as atividades abertas
            }
            else -> {
                Toast.makeText(this, "Ação desconhecida", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
