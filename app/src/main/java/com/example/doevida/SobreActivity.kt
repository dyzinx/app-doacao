package com.example.doevida

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class SobreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sobre)

        val iconeMenu: ImageView = findViewById(R.id.iconeMenu)
        val iconeUsuario: ImageView = findViewById(R.id.iconeUsuario)

        iconeMenu.setOnClickListener { view -> showStyledMenuPopup(view, R.menu.menu_main) }
        iconeUsuario.setOnClickListener { view -> showStyledMenuPopup(view, R.menu.menu_profile) }
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
            val method = menuPopupHelper.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
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
            R.id.menu_sobre -> Toast.makeText(this, "Você já está em Sobre o App", Toast.LENGTH_SHORT).show()
            R.id.menu_editar -> startActivity(Intent(this, EditarPerfilActivity::class.java))
            R.id.menu_sair -> finish()
        }
    }
}
