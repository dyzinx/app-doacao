package com.example.teladelogin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class EditarPerfilActivity : AppCompatActivity() {

    private lateinit var imagePerfil: ImageView
    private val PICK_IMAGE_REQUEST = 1
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        imagePerfil = findViewById(R.id.imagePerfil)
        val icCamera: ImageView = findViewById(R.id.ic_camera)

        // Escolher imagem do perfil
        icCamera.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Botão salvar alterações
        findViewById<Button>(R.id.btnSalvar).setOnClickListener {
            Toast.makeText(this, "Alterações salvas com sucesso!", Toast.LENGTH_SHORT).show()
            // Aqui você pode salvar os dados no Firebase Firestore se desejar
        }

        // Botão alterar senha
        findViewById<Button>(R.id.btnAlterarSenha).setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_alterar_senha, null)
            val inputEmail = dialogView.findViewById<EditText>(R.id.inputEmail)
            val inputSenhaAtual = dialogView.findViewById<EditText>(R.id.inputSenhaAtual)
            val inputNovaSenha = dialogView.findViewById<EditText>(R.id.inputNovaSenha)
            val inputConfirmarSenha = dialogView.findViewById<EditText>(R.id.inputConfirmarSenha)

            AlertDialog.Builder(this)
                .setTitle("Alterar Senha")
                .setView(dialogView)
                .setPositiveButton("Salvar") { _, _ ->
                    val email = inputEmail.text.toString().trim()
                    val senhaAtual = inputSenhaAtual.text.toString()
                    val novaSenha = inputNovaSenha.text.toString()
                    val confirmarSenha = inputConfirmarSenha.text.toString()

                    if (email.isEmpty() || senhaAtual.isEmpty() || novaSenha.isEmpty() || confirmarSenha.isEmpty()) {
                        Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    if (novaSenha != confirmarSenha) {
                        Toast.makeText(this, "As senhas não coincidem.", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    val user = auth.currentUser
                    if (user != null && user.email == email) {
                        val credential = EmailAuthProvider.getCredential(email, senhaAtual)
                        user.reauthenticate(credential)
                            .addOnCompleteListener { reauthTask ->
                                if (reauthTask.isSuccessful) {
                                    user.updatePassword(novaSenha)
                                        .addOnCompleteListener { updateTask ->
                                            if (updateTask.isSuccessful) {
                                                Toast.makeText(this, "Senha atualizada com sucesso!", Toast.LENGTH_SHORT).show()
                                            } else {
                                                Toast.makeText(this, "Erro ao atualizar senha.", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                } else {
                                    Toast.makeText(this, "Senha atual incorreta.", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(this, "Email não corresponde ao usuário logado.", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = data?.data
            imagePerfil.setImageURI(imageUri)
        }
    }
}
