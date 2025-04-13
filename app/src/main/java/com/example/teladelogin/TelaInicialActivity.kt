package com.example.teladelogin

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TelaInicialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_inicial)

        val btnAgendar = findViewById<Button>(R.id.btnAgendar)
        btnAgendar.setOnClickListener {
            Toast.makeText(this, "Agendamento em breve!", Toast.LENGTH_SHORT).show()
        }
    }
}
