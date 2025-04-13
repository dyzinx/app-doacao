package com.example.teladelogin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etNome: EditText
    private lateinit var etSenha: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnCadastro: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Corrigido o nome do layout, que deve ser activity_main.xml

        // Referências aos elementos da UI
        etNome = findViewById(R.id.et_nome)
        etSenha = findViewById(R.id.et_senha)
        btnLogin = findViewById(R.id.btn_login)
        btnCadastro = findViewById(R.id.btn_cadastro)

        // Ação do botão de Login
        btnLogin.setOnClickListener {
            val nome = etNome.text.toString()
            val senha = etSenha.text.toString()

            if (nome == "admin" && senha == "1234") {
                Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                // Aqui a navegação pode ser para a própria MainActivity ou outra Activity
                // Para fins de exemplo, vamos manter a navegação para a mesma activity
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                Toast.makeText(this, "Nome ou senha incorretos!", Toast.LENGTH_SHORT).show()
            }
        }

        // Ação do botão de Cadastro
        btnCadastro.setOnClickListener {
            Toast.makeText(this, "Tela de Cadastro em desenvolvimento!", Toast.LENGTH_SHORT).show()
        }
    }
}
