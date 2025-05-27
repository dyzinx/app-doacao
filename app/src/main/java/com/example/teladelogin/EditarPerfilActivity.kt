package com.example.teladelogin

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.View
import android.os.Bundle
import android.provider.MediaStore
import android.view.ContextThemeWrapper
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.io.FileOutputStream

class EditarPerfilActivity : AppCompatActivity() {

    private lateinit var imagePerfil: ImageView
    private lateinit var edtNome: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtCelular: EditText
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        imagePerfil = findViewById(R.id.imagePerfil)
        val icCamera: ImageView = findViewById(R.id.ic_camera)
        edtNome = findViewById(R.id.edtNome)
        edtEmail = findViewById(R.id.edtEmail)
        edtCelular = findViewById(R.id.edtCelular)

        val iconeMenu: ImageView = findViewById(R.id.iconeMenu)
        val iconeUsuario: ImageView = findViewById(R.id.iconeUsuario)

        val user = auth.currentUser ?: return
        edtEmail.isEnabled = false

        // Carregar dados e imagem local
        db.collection("usuarios").document(user.uid).get().addOnSuccessListener { doc ->
            edtNome.setText(doc.getString("nome"))
            edtEmail.setText(doc.getString("email"))
            edtCelular.setText(doc.getString("celular"))

            val bitmap = carregarImagemLocal(user.uid)
            bitmap?.let {
                imagePerfil.setImageBitmap(it)
            }
        }

        icCamera.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        findViewById<Button>(R.id.btnSalvar).setOnClickListener {
            val nome = edtNome.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val celular = edtCelular.text.toString().trim()

            if (nome.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Preencha os campos obrigatórios.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userMap = hashMapOf(
                "nome" to nome,
                "email" to email,
                "celular" to celular
            )

            db.collection("usuarios").document(user.uid).set(userMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Alterações salvas com sucesso!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, TelaInicialActivity::class.java)) // Ajuste para sua Activity inicial
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao salvar dados.", Toast.LENGTH_SHORT).show()
                }

            imageUri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                salvarImagemLocal(bitmap, user.uid)
            }
        }

        // Configurar clique no menu da top bar com PopupMenu estilizado
        iconeMenu.setOnClickListener {
            showStyledMenuPopup(it, R.menu.menu_main)
        }

        // Configurar clique no ícone de usuário
        iconeUsuario.setOnClickListener {
            showStyledMenuPopup(it, R.menu.menu_profile)
        }

        findViewById<Button>(R.id.btnAlterarSenha).setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_alterar_senha, null)
            val dialog = AlertDialog.Builder(this).setView(dialogView).create()

            val inputEmail = dialogView.findViewById<EditText>(R.id.inputEmail)
            val inputSenhaAtual = dialogView.findViewById<EditText>(R.id.inputSenhaAtual)
            val inputNovaSenha = dialogView.findViewById<EditText>(R.id.inputNovaSenha)
            val inputConfirmarSenha = dialogView.findViewById<EditText>(R.id.inputConfirmarSenha)
            val btnSalvarSenha = dialogView.findViewById<Button>(R.id.btnSalvarSenha)

            btnSalvarSenha.setOnClickListener {
                val email = inputEmail.text.toString().trim()
                val senhaAtual = inputSenhaAtual.text.toString()
                val novaSenha = inputNovaSenha.text.toString()
                val confirmarSenha = inputConfirmarSenha.text.toString()

                if (novaSenha != confirmarSenha) {
                    Toast.makeText(this, "As senhas não coincidem.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (email.isEmpty() || senhaAtual.isEmpty() || novaSenha.isEmpty()) {
                    Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val credential = EmailAuthProvider.getCredential(email, senhaAtual)
                auth.currentUser?.reauthenticate(credential)?.addOnCompleteListener { reauth ->
                    if (reauth.isSuccessful) {
                        auth.currentUser?.updatePassword(novaSenha)?.addOnCompleteListener { update ->
                            if (update.isSuccessful) {
                                Toast.makeText(this, "Senha atualizada com sucesso!", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            } else {
                                Toast.makeText(this, "Erro ao atualizar senha.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Reautenticação falhou. Verifique o email e senha atual.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            dialog.show()
        }
    }

    private fun showStyledMenuPopup(anchorView: View, menuRes: Int) {
        val wrapper = ContextThemeWrapper(this, R.style.PopupMenuStyle)
        val popup = PopupMenu(wrapper, anchorView)
        popup.menuInflater.inflate(menuRes, popup.menu)

        // Set icons for menu items
        for (i in 0 until popup.menu.size()) {
            popup.menu.getItem(i).icon = ContextCompat.getDrawable(
                this,
                getIconForMenuItem(popup.menu.getItem(i).itemId)
            )
        }

        // Force icons to show
        try {
            val fields = popup.javaClass.declaredFields
            for (field in fields) {
                if ("mPopup" == field.name) {
                    field.isAccessible = true
                    val menuPopupHelper = field.get(popup)
                    val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                    val setForceIcons = classPopupHelper.getMethod("setForceShowIcon", Boolean::class.javaPrimitiveType)
                    setForceIcons.invoke(menuPopupHelper, true)
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        popup.setOnMenuItemClickListener {
            handleMenuItemClick(it.itemId)
            true
        }
        popup.show()
    }

    private fun getIconForMenuItem(itemId: Int): Int {
        return when (itemId) {
            R.id.menu_historico -> R.drawable.ic_histor
            R.id.menu_vidas -> R.drawable.ic_life
            R.id.menu_config -> R.drawable.ic_settings
            R.id.menu_sobre -> R.drawable.ic_about
            R.id.menu_editar -> R.drawable.ic_edit
            R.id.menu_sair -> R.drawable.logout
            else -> 0
        }
    }

    private fun handleMenuItemClick(itemId: Int) {
        when (itemId) {
            R.id.menu_historico -> startActivity(Intent(this, HistoricoDoacoesActivity::class.java))
            R.id.menu_vidas -> Toast.makeText(this, "Você já está em Vidas Salvas", Toast.LENGTH_SHORT).show()
            R.id.menu_config -> startActivity(Intent(this, ConfiguracoesActivity::class.java))
            R.id.menu_sobre -> startActivity(Intent(this, SobreActivity::class.java))
            R.id.menu_editar -> startActivity(Intent(this, EditarPerfilActivity::class.java))
            R.id.menu_sair -> finishAffinity()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            imagePerfil.setImageURI(imageUri)
        }
    }

    private fun salvarImagemLocal(bitmap: Bitmap, userId: String) {
        val file = File(filesDir, "${userId}_perfil.jpg")
        val output = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)
        output.flush()
        output.close()
    }

    private fun carregarImagemLocal(userId: String): Bitmap? {
        val file = File(filesDir, "${userId}_perfil.jpg")
        return if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null
    }
}
