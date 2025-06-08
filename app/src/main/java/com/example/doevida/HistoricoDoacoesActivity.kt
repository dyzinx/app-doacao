package com.example.doevida

import android.content.Intent
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HistoricoDoacoesActivity : AppCompatActivity() {

    private lateinit var btnMenu: ImageView
    private lateinit var btnUsuario: ImageView
    private lateinit var containerAgendamentos: LinearLayout
    private lateinit var containerRealizadas: LinearLayout
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historico_doacoes)

        btnMenu = findViewById(R.id.btnMenu)
        btnUsuario = findViewById(R.id.btnUsuario)
        containerAgendamentos = findViewById(R.id.containerAgendamentos)
        containerRealizadas = findViewById(R.id.containerRealizadas)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        btnMenu.setOnClickListener { showStyledMenuPopup(it, R.menu.menu_main) }
        btnUsuario.setOnClickListener { showStyledMenuPopup(it, R.menu.menu_profile) }

        carregarAgendamentos(currentUser.uid)
        carregarRealizadas(currentUser.uid)
    }

    private fun carregarAgendamentos(uid: String) {
        db.collection("usuarios").document(uid)
            .collection("agendamentos")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                containerAgendamentos.removeAllViews()

                for (document in result) {
                    val hospital = document.getString("hospital") ?: "N/A"
                    val data = document.getString("data") ?: "N/A"
                    val agendamentoId = document.id

                    val itemView = layoutInflater.inflate(R.layout.item_doacao_agendada, null)
                    val txtInfo = itemView.findViewById<TextView>(R.id.txtInfoAgendamento)
                    val btnComprovante = itemView.findViewById<Button>(R.id.btnVerComprovante)

                    txtInfo.text = "Doação agendada para $data\nLocal: $hospital"

                    btnComprovante.setOnClickListener {
                        val intent = Intent(this, ComprovanteAgendamentoActivity::class.java)
                        intent.putExtra("agendamentoId", agendamentoId)
                        startActivity(intent)
                    }

                    containerAgendamentos.addView(itemView)
                }

                if (result.isEmpty) {
                    Toast.makeText(this, "Nenhuma doação agendada encontrada", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao carregar agendamentos", Toast.LENGTH_LONG).show()
            }
    }

    private fun carregarRealizadas(uid: String) {
        db.collection("usuarios").document(uid)
            .collection("realizadas")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                containerRealizadas.removeAllViews()

                for (document in result) {
                    val hospital = document.getString("hospital") ?: "N/A"
                    val data = document.getString("data") ?: "N/A"

                    val itemView = layoutInflater.inflate(R.layout.item_doacao_realizada, null)
                    val txtInfo = itemView.findViewById<TextView>(R.id.txtInfoRealizada)

                    txtInfo.text = "Doação realizada no dia $data\nLocal: $hospital"

                    containerRealizadas.addView(itemView)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao carregar doações realizadas", Toast.LENGTH_LONG).show()
            }
    }

    private fun showStyledMenuPopup(anchorView: View, menuRes: Int) {
        val wrapper = ContextThemeWrapper(this, R.style.PopupMenuStyle)
        val popup = PopupMenu(wrapper, anchorView)
        popup.menuInflater.inflate(menuRes, popup.menu)

        for (i in 0 until popup.menu.size()) {
            popup.menu.getItem(i)?.icon?.setTint(
                ContextCompat.getColor(this, android.R.color.black)
            )
        }

        popup.setOnMenuItemClickListener {
            handleMenuItemClick(it.itemId)
            true
        }

        forceShowIcons(popup)
        popup.show()
    }

    private fun forceShowIcons(popup: PopupMenu) {
        try {
            val fields = popup.javaClass.declaredFields
            for (field in fields) {
                if (field.name == "mPopup") {
                    field.isAccessible = true
                    val menuPopupHelper = field.get(popup)
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
            R.id.menu_historico -> startActivity(Intent(this, HistoricoDoacoesActivity::class.java))
            R.id.menu_vidas -> startActivity(Intent(this, VidasSalvasActivity::class.java))
            R.id.menu_sobre -> startActivity(Intent(this, SobreActivity::class.java))
            R.id.menu_editar -> startActivity(Intent(this, EditarPerfilActivity::class.java))
            R.id.menu_sair -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            }
        }
    }
}
