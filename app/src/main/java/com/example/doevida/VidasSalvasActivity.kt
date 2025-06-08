package com.example.doevida

import android.content.Intent
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat

class VidasSalvasActivity : AppCompatActivity() {

    private lateinit var listaEstados: ListView
    private lateinit var containerInfoEstado: LinearLayout
    private lateinit var tituloEstado: TextView
    private lateinit var vidasSalvasEstado: TextView
    private lateinit var mensagemEstado: TextView
    private lateinit var iconeMenu: ImageView
    private lateinit var iconeUsuario: ImageView

    private val estados = listOf(
        "Acre (AC)", "Alagoas (AL)", "Amapá (AP)", "Amazonas (AM)", "Bahia (BA)", "Ceará (CE)", "Distrito Federal (DF)",
        "Espírito Santo (ES)", "Goiás (GO)", "Maranhão (MA)", "Mato Grosso (MT)", "Mato Grosso do Sul (MS)",
        "Minas Gerais (MG)", "Pará (PA)", "Paraíba (PB)", "Paraná (PR)", "Pernambuco (PE)", "Piauí (PI)",
        "Rio de Janeiro (RJ)", "Rio Grande do Norte (RN)", "Rio Grande do Sul (RS)", "Rondônia (RO)", "Roraima (RR)",
        "Santa Catarina (SC)", "São Paulo (SP)", "Sergipe (SE)", "Tocantins (TO)"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vidas_salvas)

        listaEstados = findViewById(R.id.listaEstados)
        containerInfoEstado = findViewById(R.id.containerInfoEstado)
        tituloEstado = findViewById(R.id.tituloEstado)
        vidasSalvasEstado = findViewById(R.id.vidasSalvasEstado)
        mensagemEstado = findViewById(R.id.mensagemEstado)
        iconeMenu = findViewById(R.id.iconeMenu)
        iconeUsuario = findViewById(R.id.iconeUsuario)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, estados)
        listaEstados.adapter = adapter

        listaEstados.setOnItemClickListener { _, _, position, _ ->
            val estado = estados[position]
            val (vidas, mensagem) = getInfoByState(estado)

            tituloEstado.text = estado
            vidasSalvasEstado.text = "$vidas vidas salvas"
            mensagemEstado.text = mensagem
            containerInfoEstado.visibility = View.VISIBLE
        }

        iconeMenu.setOnClickListener { showStyledMenuPopup(it, R.menu.menu_main) }
        iconeUsuario.setOnClickListener { showStyledMenuPopup(it, R.menu.menu_profile) }
    }

    private fun getInfoByState(stateName: String): Pair<String, String> {
        return when (stateName) {
            "São Paulo (SP)" -> "15.000" to "SP salvando vidas todos os dias."
            "Rio de Janeiro (RJ)" -> "10.000" to "Rio de esperança!"
            "Minas Gerais (MG)" -> "12.000" to "Minas que brilham com solidariedade."
            "Bahia (BA)" -> "8.000" to "A Bahia pulsa com amor ao próximo."
            "Paraná (PR)" -> "7.500" to "Cada gota conta."
            "Rio Grande do Sul (RS)" -> "6.000" to "Gaúcho que salva vidas."
            "Pernambuco (PE)" -> "5.000" to "Pernambuco em corrente do bem."
            "Distrito Federal (DF)" -> "4.500" to "O coração do Brasil bate mais forte com você."
            "Ceará (CE)" -> "4.500" to "Compartilhe o que você tem de melhor."
            "Santa Catarina (SC)" -> "4.300" to "Solidariedade catarinense!"
            "Pará (PA)" -> "3.800" to "Dê o melhor de si."
            "Espírito Santo (ES)" -> "3.200" to "Gesto pequeno, impacto gigante."
            "Amazonas (AM)" -> "3.000" to "Sua solidariedade corre nas veias."
            "Goiás (GO)" -> "5.000" to "Uma doação, uma nova chance."
            "Maranhão (MA)" -> "2.800" to "Seja herói na vida de alguém."
            "Mato Grosso (MT)" -> "2.700" to "Doar sangue é doar esperança."
            "Mato Grosso do Sul (MS)" -> "2.600" to "Você pode salvar até 4 vidas."
            "Paraíba (PB)" -> "2.200" to "Ajude a PB a salvar mais vidas."
            "Rio Grande do Norte (RN)" -> "2.100" to "Doar é um ato de amor."
            "Alagoas (AL)" -> "2.000" to "Você é a esperança de alguém."
            "Piauí (PI)" -> "2.000" to "Pequenos atos, grandes mudanças."
            "Sergipe (SE)" -> "1.900" to "Seja parte dessa corrente do bem."
            "Rondônia (RO)" -> "1.800" to "Ajude quem mais precisa."
            "Tocantins (TO)" -> "1.600" to "Você pode ser a diferença."
            "Acre (AC)" -> "1.500" to "Toda doação ajuda a mudar histórias."
            "Amapá (AP)" -> "1.200" to "Doe sangue, doe vida."
            "Roraima (RR)" -> "1.100" to "Doe sangue, espalhe amor."
            else -> "0" to "Ainda não há dados disponíveis para este estado."
        }
    }

    private fun showStyledMenuPopup(anchorView: View, menuRes: Int) {
        val wrapper = ContextThemeWrapper(this, R.style.PopupMenuStyle)
        val popup = PopupMenu(wrapper, anchorView)
        popup.menuInflater.inflate(menuRes, popup.menu)

        // Set icons for menu items
        for (i in 0 until popup.menu.size()) {
            popup.menu.getItem(i).icon = ContextCompat.getDrawable(
                this@VidasSalvasActivity,
                getIconForMenuItem(popup.menu.getItem(i).itemId)
            )
        }

        // Force icons to show
        try {
            val fields = popup.javaClass.declaredFields
            for (field in fields) {
                if ("mPopup" == field.name) {
                    field.isAccessible = true
                    val menuPopupHelper = field.get(popup)
                    val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                    val setForceIcons = classPopupHelper.getMethod("setForceShowIcon", Boolean::class.javaPrimitiveType)
                    setForceIcons.invoke(menuPopupHelper, true)
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        popup.setOnMenuItemClickListener {
            handleMenuItemClick(it.itemId)
            true
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
            R.id.menu_vidas -> Toast.makeText(this, "Você já está em Vidas Salvas", Toast.LENGTH_SHORT).show()
            R.id.menu_sobre -> startActivity(Intent(this, SobreActivity::class.java))
            R.id.menu_editar -> startActivity(Intent(this, EditarPerfilActivity::class.java))
            R.id.menu_sair -> finishAffinity()
        }
    }
}
