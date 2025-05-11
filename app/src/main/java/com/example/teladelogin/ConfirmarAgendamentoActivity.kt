package com.example.teladelogin

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

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

        val nomeHospital = intent.getStringExtra("nomeHospital")
        val enderecoHospital = intent.getStringExtra("enderecoHospital")
        val dataSelecionada = intent.getStringExtra("dataSelecionada")
        val imagemUrl = intent.getStringExtra("imagemUrl")

        editLocal.setText("$nomeHospital - $enderecoHospital")
        editData.setText(dataSelecionada)

        if (!imagemUrl.isNullOrEmpty()) {
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
            val horarioSelecionado = spinnerHorario.selectedItem.toString()

            val intent = Intent(this, AgendamentoConcluidoActivity::class.java)
            intent.putExtra("horarioSelecionado", horarioSelecionado)
            intent.putExtra("nomeHospital", nomeHospital)
            intent.putExtra("dataSelecionada", dataSelecionada)
            startActivity(intent)
            finish()
        }
    }
}
