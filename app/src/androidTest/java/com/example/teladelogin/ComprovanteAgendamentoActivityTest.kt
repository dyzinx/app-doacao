package com.example.teladelogin

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ComprovanteAgendamentoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comprovante_agendamento)

        // Obtém os dados da Intent com valores padrão
        val nomeCatador = intent.getStringExtra("nomeCatador") ?: "Nome não informado"
        val materialCatador = intent.getStringExtra("materialCatador") ?: "Material não informado"
        val horarioAgendado = intent.getStringExtra("horarioAgendado") ?: "Horário não informado"

        // Monta o texto de resumo
        val textoResumo = """
            Nome: $nomeCatador
            Material: $materialCatador
            Horário: $horarioAgendado
        """.trimIndent()

        // Define o texto no TextView
        val txtResumoComprovante = findViewById<TextView>(R.id.txtResumoComprovante)
        txtResumoComprovante.text = textoResumo

        // Configura botão de "Doação Concluída"
        val btnDoacaoConcluida = findViewById<Button>(R.id.btnDoacaoConcluida)
        btnDoacaoConcluida.setOnClickListener {
            // Ação ao concluir (aqui apenas fecha a tela)
            finish()
        }
    }
}
