package com.example.teladelogin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AgendamentoConcluidoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agendamento_concluido)

        val btnMeusAgendamentos = findViewById<Button>(R.id.btnMeusAgendamentos)

        btnMeusAgendamentos.setOnClickListener {
            val intent = Intent(this, HistoricoDoacoesActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
