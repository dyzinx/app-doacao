package com.example.teladelogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ComprovanteAgendamentoActivity : AppCompatActivity() {

    private lateinit var txtResumo: TextView
    private lateinit var btnConcluir: Button
    private lateinit var btnMenu: ImageView
    private lateinit var btnUsuario: ImageView
    private lateinit var db: FirebaseFirestore
    private lateinit var agendamentoId: String
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comprovante_agendamento)

        txtResumo = findViewById(R.id.txtResumoComprovante)
        btnConcluir = findViewById(R.id.btnDoacaoConcluida)
        btnMenu = findViewById(R.id.btnMenu)
        btnUsuario = findViewById(R.id.btnUsuario)

        db = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        agendamentoId = intent.getStringExtra("agendamentoId") ?: ""

        if (uid.isBlank() || agendamentoId.isBlank()) {
            Toast.makeText(this, "Erro ao carregar dados do comprovante.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Carrega os dados para exibir
        db.collection("usuarios").document(uid)
            .collection("agendamentos").document(agendamentoId)
            .get()
            .addOnSuccessListener { doc ->
                val hospital = doc.getString("hospital") ?: "N/A"
                val data = doc.getString("data") ?: "N/A"
                val horario = doc.getString("horario") ?: "N/A"
                val questionario = doc.get("questionario") as? Map<*, *>

                val resumo = buildString {
                    append("📅 Hospital: $hospital\n📆 Data: $data\n⏰ Horário: $horario\n\n")
                    append("📋 Questionário:\n")
                    questionario?.forEach { (key, value) ->
                        append("• $key: $value\n")
                    }
                }

                txtResumo.text = resumo
            }

        // Concluir doação
        btnConcluir.setOnClickListener {
            val userRef = db.collection("usuarios").document(uid)
            val agendamentoRef = userRef.collection("agendamentos").document(agendamentoId)

            agendamentoRef.get().addOnSuccessListener { document ->
                val dados = document.data
                if (dados != null) {
                    val dadosComTimestamp = HashMap(dados)
                    dadosComTimestamp["timestamp"] = Timestamp.now()

                    userRef.collection("realizadas").add(dadosComTimestamp)
                        .addOnSuccessListener {
                            agendamentoRef.delete().addOnSuccessListener {
                                Toast.makeText(this, "Doação marcada como realizada!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, HistoricoDoacoesActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                            }.addOnFailureListener {
                                Toast.makeText(this, "Erro ao excluir agendamento", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Erro ao mover para realizadas: ${e.message}", Toast.LENGTH_LONG).show()
                            Log.e("FIREBASE", "Erro ao mover para realizadas", e)
                        }
                } else {
                    Toast.makeText(this, "Agendamento não encontrado", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Erro ao acessar o agendamento", Toast.LENGTH_SHORT).show()
            }
        }

        // Menu
        btnMenu.setOnClickListener { showStyledMenuPopup(it, R.menu.menu_main) }
        btnUsuario.setOnClickListener { showStyledMenuPopup(it, R.menu.menu_profile) }
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

    private fun handleMenuItemClick(itemId: Int) {
        when (itemId) {
            R.id.menu_historico -> startActivity(Intent(this, HistoricoDoacoesActivity::class.java))
            R.id.menu_vidas -> startActivity(Intent(this, VidasSalvasActivity::class.java))
            R.id.menu_config -> startActivity(Intent(this, ConfiguracoesActivity::class.java))
            R.id.menu_sobre -> startActivity(Intent(this, SobreActivity::class.java))
            R.id.menu_editar -> startActivity(Intent(this, EditarPerfilActivity::class.java))
            R.id.menu_sair -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            }
        }
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
}
