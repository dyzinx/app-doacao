package com.example.teladelogin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class CadastroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_cadastro)

        val editNome = findViewById<EditText>(R.id.editNome)
        val editSobrenome = findViewById<EditText>(R.id.editSobrenome)
        val editCPF = findViewById<EditText>(R.id.editCPF)
        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editTelefone = findViewById<EditText>(R.id.editTelefone)
        val editSenha = findViewById<EditText>(R.id.editSenha)
        val editConfirmarSenha = findViewById<EditText>(R.id.editConfirmarSenha)
        val btnCadastrar = findViewById<Button>(R.id.btnCadastrar)

        val db = FirebaseFirestore.getInstance()

        btnCadastrar.setOnClickListener {
            val nome = editNome.text.toString()
            val sobrenome = editSobrenome.text.toString()
            val cpf = editCPF.text.toString()
            val email = editEmail.text.toString()
            val telefone = editTelefone.text.toString()
            val senha = editSenha.text.toString()
            val confirmarSenha = editConfirmarSenha.text.toString()

            if (nome.isBlank() || sobrenome.isBlank() || cpf.isBlank() ||
                email.isBlank() || telefone.isBlank() || senha.isBlank() || confirmarSenha.isBlank()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            } else if (senha != confirmarSenha) {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
            } else {
                val usuario = hashMapOf(
                    "nome" to nome,
                    "sobrenome" to sobrenome,
                    "cpf" to cpf,
                    "email" to email,
                    "telefone" to telefone,
                    "senha" to senha
                )

                db.collection("usuarios")
                    .add(usuario)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Erro ao cadastrar: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
