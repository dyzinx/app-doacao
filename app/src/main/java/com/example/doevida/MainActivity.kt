package com.example.doevida

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etSenha: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnCadastro: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etEmail = findViewById(R.id.et_email)
        etSenha = findViewById(R.id.et_senha)
        btnLogin = findViewById(R.id.btn_login)
        btnCadastro = findViewById(R.id.btn_cadastro)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val senha = etSenha.text.toString().trim()

            if (email.isBlank() || senha.isBlank()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnLogin.isEnabled = false

            auth.signInWithEmailAndPassword(email, senha)
                .addOnSuccessListener {
                    val uid = auth.currentUser?.uid
                    if (uid != null) {
                        Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, TelaInicialActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Erro: UID inválido.", Toast.LENGTH_SHORT).show()
                        btnLogin.isEnabled = true
                    }
                }
                .addOnFailureListener { e ->
                    btnLogin.isEnabled = true
                    when {
                        e.message?.contains("password is invalid") == true -> {
                            Toast.makeText(this, "Senha incorreta", Toast.LENGTH_SHORT).show()
                        }
                        e.message?.contains("no user record") == true -> {
                            Toast.makeText(this, "Usuário não encontrado", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(this, "Erro: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }

        btnCadastro.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }
    }
}
