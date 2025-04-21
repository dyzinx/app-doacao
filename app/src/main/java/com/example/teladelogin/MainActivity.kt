package com.example.teladelogin

import com.example.teladelogin.TelaInicialActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var etNome: EditText
    private lateinit var etSenha: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnCadastro: Button
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_main)

        etNome = findViewById(R.id.et_nome)
        etSenha = findViewById(R.id.et_senha)
        btnLogin = findViewById(R.id.btn_login)
        btnCadastro = findViewById(R.id.btn_cadastro)

        db = FirebaseFirestore.getInstance()

        btnLogin.setOnClickListener {
            val nome = etNome.text.toString()
            val senha = etSenha.text.toString()

            if (nome.isBlank() || senha.isBlank()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            db.collection("usuarios")
                .whereEqualTo("nome", nome)
                .whereEqualTo("senha", senha)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                        try {
                            val intent = Intent(this, TelaInicialActivity::class.java)
                            startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(this, "Erro ao abrir tela: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this, "Credenciais inválidas", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Erro: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }

        btnCadastro.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }
    }
}