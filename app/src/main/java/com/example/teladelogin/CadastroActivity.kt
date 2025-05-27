package com.example.teladelogin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CadastroActivity : AppCompatActivity() {

    private lateinit var editNome: EditText
    private lateinit var editSobrenome: EditText
    private lateinit var editCPF: EditText
    private lateinit var editEmail: EditText
    private lateinit var editTelefone: EditText
    private lateinit var editSenha: EditText
    private lateinit var editConfirmarSenha: EditText
    private lateinit var btnCadastrar: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_cadastro)

        // Inicializa campos
        editNome = findViewById(R.id.editNome)
        editSobrenome = findViewById(R.id.editSobrenome)
        editCPF = findViewById(R.id.editCPF)
        editEmail = findViewById(R.id.editEmail)
        editTelefone = findViewById(R.id.editTelefone)
        editSenha = findViewById(R.id.editSenha)
        editConfirmarSenha = findViewById(R.id.editConfirmarSenha)
        btnCadastrar = findViewById(R.id.btnCadastrar)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        btnCadastrar.setOnClickListener {
            val nome = editNome.text.toString().trim()
            val sobrenome = editSobrenome.text.toString().trim()
            val cpf = editCPF.text.toString().trim()
            val email = editEmail.text.toString().trim()
            val telefone = editTelefone.text.toString().trim()
            val senha = editSenha.text.toString()
            val confirmarSenha = editConfirmarSenha.text.toString()

            // Validação de campos
            if (nome.isBlank() || sobrenome.isBlank() || cpf.isBlank() ||
                email.isBlank() || telefone.isBlank() || senha.isBlank() || confirmarSenha.isBlank()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (senha != confirmarSenha) {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (senha.length < 6) {
                Toast.makeText(this, "A senha deve ter pelo menos 6 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnCadastrar.isEnabled = false

            auth.createUserWithEmailAndPassword(email, senha)
                .addOnSuccessListener { result ->
                    val uid = result.user?.uid
                    if (uid != null) {
                        val usuario = hashMapOf(
                            "nome" to nome,
                            "sobrenome" to sobrenome,
                            "cpf" to cpf,
                            "email" to email,
                            "telefone" to telefone
                        )

                        db.collection("usuarios").document(uid)
                            .set(usuario)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, TelaInicialActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener { e ->
                                btnCadastrar.isEnabled = true
                                Toast.makeText(this, "Erro ao salvar no banco: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        btnCadastrar.isEnabled = true
                        Toast.makeText(this, "Erro ao obter UID do usuário.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    btnCadastrar.isEnabled = true
                    Toast.makeText(this, "Erro ao criar usuário: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
