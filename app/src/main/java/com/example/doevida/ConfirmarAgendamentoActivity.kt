package com.example.doevida

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp

class ConfirmarAgendamentoActivity : AppCompatActivity() {

    private lateinit var imageHospital: ImageView
    private lateinit var editLocal: EditText
    private lateinit var editData: EditText
    private lateinit var spinnerHorario: Spinner
    private lateinit var btnConfirmar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmar_agendamento)

        imageHospital = findViewById(R.id.imageHospital)
        editLocal = findViewById(R.id.editLocal)
        editData = findViewById(R.id.editData)
        spinnerHorario = findViewById(R.id.spinnerHorario)
        btnConfirmar = findViewById(R.id.btnConfirmar)

        val nomeHospital = intent.getStringExtra("nomeHospital") ?: "Hospital não informado"
        val enderecoHospital = intent.getStringExtra("enderecoHospital") ?: "Endereço não informado"
        val dataSelecionada = intent.getStringExtra("dataSelecionada") ?: "Data não informada"
        val imagemUrl = intent.getStringExtra("imagemUrl") ?: ""

        val tipoSanguineo = intent.getStringExtra("Tipo Sanguíneo") ?: "Não informado"
        val genero = intent.getStringExtra("Gênero") ?: "Não informado"
        val estadoCivil = intent.getStringExtra("Estado Civil") ?: "Não informado"
        val nacionalidade = intent.getStringExtra("Nacionalidade") ?: "Não informado"
        val alergias = intent.getStringExtra("Possui Alergias") ?: "Não informado"
        val doencasCronicas = intent.getStringExtra("Tem Doença Crônica") ?: "Não informado"
        val vacinado = intent.getStringExtra("Vacinado") ?: "Não informado"
        val fuma = intent.getStringExtra("Fuma") ?: "Não informado"
        val bebidasAlcoolicas = intent.getStringExtra("Bebidas Alcoólicas") ?: "Não informado"
        val alergiaRemedios = intent.getStringExtra("Alergia a Remédios") ?: "Não informado"
        val atividadesFisicas = intent.getStringExtra("Atividades Físicas") ?: "Não informado"
        val tatuagemRecente = intent.getStringExtra("Tatuagem Recente") ?: "Não informado"

        editLocal.setText("$nomeHospital - $enderecoHospital")
        editData.setText(dataSelecionada)

        if (imagemUrl.isNotEmpty()) {
            Glide.with(this).load(imagemUrl).into(imageHospital)
        } else {
            imageHospital.setImageResource(R.drawable.imagem_padrao_hospital)
        }

        editData.setOnClickListener {
            finish()
        }

        val horariosDisponiveis = listOf(
            "08:00", "08:30", "09:00", "09:30",
            "10:00", "10:30", "11:00", "11:30",
            "14:00", "14:30", "15:00"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, horariosDisponiveis)
        spinnerHorario.adapter = adapter

        btnConfirmar.setOnClickListener {
            val horarioSelecionado = spinnerHorario.selectedItem?.toString() ?: "Horário não selecionado"
            val uid = FirebaseAuth.getInstance().currentUser?.uid

            if (uid == null) {
                Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val agendamento = hashMapOf(
                "hospital" to nomeHospital,
                "endereco" to enderecoHospital,
                "data" to dataSelecionada,
                "horario" to horarioSelecionado,
                "imagemUrl" to imagemUrl,
                "timestamp" to Timestamp.now(),
                "questionario" to hashMapOf(
                    "Tipo Sanguíneo" to tipoSanguineo,
                    "Gênero" to genero,
                    "Estado Civil" to estadoCivil,
                    "Nacionalidade" to nacionalidade,
                    "Possui Alergias" to alergias,
                    "Tem Doença Crônica" to doencasCronicas,
                    "Vacinado" to vacinado,
                    "Fuma" to fuma,
                    "Bebidas Alcoólicas" to bebidasAlcoolicas,
                    "Alergia a Remédios" to alergiaRemedios,
                    "Atividades Físicas" to atividadesFisicas,
                    "Tatuagem Recente" to tatuagemRecente
                )
            )

            FirebaseFirestore.getInstance()
                .collection("usuarios")
                .document(uid)
                .collection("agendamentos")
                .add(agendamento)
                .addOnSuccessListener {
                    val intent = Intent(this, AgendamentoConcluidoActivity::class.java)
                    intent.putExtra("horarioSelecionado", horarioSelecionado)
                    intent.putExtra("nomeHospital", nomeHospital)
                    intent.putExtra("dataSelecionada", dataSelecionada)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Erro ao salvar agendamento: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}