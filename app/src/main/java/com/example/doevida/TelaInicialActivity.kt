package com.example.doevida

import android.content.Intent
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class TelaInicialActivity : AppCompatActivity() {

    private lateinit var btnAgendar: Button
    private lateinit var iconeMenu: ImageView
    private lateinit var iconeUsuario: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_inicial)

        btnAgendar = findViewById(R.id.btnAgendar)
        iconeMenu = findViewById(R.id.iconeMenu)
        iconeUsuario = findViewById(R.id.iconeUsuario)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        btnAgendar.setOnClickListener {
            val intent = Intent(this, QuestionarioActivity::class.java)
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
        // Cria um contexto com o tema personalizado
        val wrapper = ContextThemeWrapper(this, R.style.PopupMenuStyle)

        // Cria o PopupMenu com o contexto temático
        val popup = PopupMenu(wrapper, anchorView).apply {
            // Infla o menu a partir do XML
            menuInflater.inflate(menuRes, menu)

            // Configura os ícones
            for (i in 0 until menu.size()) {
                menu.getItem(i)?.let { item ->
                    item.icon = ContextCompat.getDrawable(this@TelaInicialActivity, getIconForMenuItem(item.itemId))
                    item.icon?.setTint(ContextCompat.getColor(this@TelaInicialActivity, android.R.color.black))
                }
            }

            setOnMenuItemClickListener { item ->
                handleMenuItemClick(item.itemId)
                true
            }

            // Força mostrar ícones (para APIs antigas)
            forceShowIcons()
            show()
        }
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
            R.id.menu_historico -> {
                val intent = Intent(this, HistoricoDoacoesActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_vidas -> {
                val intent = Intent(this, VidasSalvasActivity::class.java)
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