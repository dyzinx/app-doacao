package com.example.doevida

import android.content.Intent
import android.widget.Toast
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class QuestionarioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionario)

        setupAllSpinners()

        val btnConcluir = findViewById<Button>(R.id.btnConcluir)
        btnConcluir.setOnClickListener {
            if (validarFormulario()) {
                avancarParaProximaTela()
            } else {
                Toast.makeText(this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setupAllSpinners() {
        setupSpinner(R.id.spinnerTipoSanguineo, R.array.tipos_sanguineos)
        setupSpinner(R.id.spinnerGenero, R.array.generos)
        setupSpinner(R.id.spinnerEstadoCivil, R.array.estados_civis)
        setupSpinner(R.id.spinnerNacionalidade, R.array.nacionalidades)
        setupSpinner(R.id.spinnerAlergias, R.array.sim_nao)
        setupSpinner(R.id.spinnerDoencasCronicas, R.array.sim_nao)
        setupSpinner(R.id.spinnerVacinado, R.array.sim_nao)
        setupSpinner(R.id.spinnerFuma, R.array.sim_nao)
        setupSpinner(R.id.spinnerBebidasAlcoolicas, R.array.sim_nao)
        setupSpinner(R.id.spinnerAlergiaRemedios, R.array.sim_nao)
        setupSpinner(R.id.spinnerAtividadesFisicas, R.array.frequencia_atividades)
        setupSpinner(R.id.spinnerTatuagemRecente, R.array.sim_nao)
    }

    private fun setupSpinner(spinnerId: Int, arrayResource: Int) {
        val spinner = findViewById<Spinner>(spinnerId)
        ArrayAdapter.createFromResource(
            this,
            arrayResource,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    private fun validarFormulario(): Boolean {
        val camposObrigatorios = listOf(
            R.id.spinnerTipoSanguineo,
            R.id.spinnerGenero,
            R.id.spinnerEstadoCivil,
            R.id.spinnerNacionalidade,
            R.id.spinnerAlergias,
            R.id.spinnerDoencasCronicas,
            R.id.spinnerVacinado,
            R.id.spinnerFuma,
            R.id.spinnerBebidasAlcoolicas,
            R.id.spinnerAlergiaRemedios,
            R.id.spinnerAtividadesFisicas,
            R.id.spinnerTatuagemRecente
        )

        for (id in camposObrigatorios) {
            val spinner = findViewById<Spinner>(id)
            if (spinner.selectedItemPosition == 0) {
                return false
            }
        }
        return true
    }

    private fun validarElegibilidade(): Boolean {
        return findViewById<Spinner>(R.id.spinnerAlergias).selectedItemPosition == 2 &&
                findViewById<Spinner>(R.id.spinnerDoencasCronicas).selectedItemPosition == 2 &&
                findViewById<Spinner>(R.id.spinnerFuma).selectedItemPosition == 2 &&
                findViewById<Spinner>(R.id.spinnerBebidasAlcoolicas).selectedItemPosition == 2 &&
                findViewById<Spinner>(R.id.spinnerTatuagemRecente).selectedItemPosition == 2
    }

    private fun avancarParaProximaTela() {
        if (validarElegibilidade()) {
            val intent = Intent(this, SelecaoHospitalActivity::class.java).apply {
                putExtra("Tipo Sanguíneo", getSpinnerValue(R.id.spinnerTipoSanguineo))
                putExtra("Gênero", getSpinnerValue(R.id.spinnerGenero))
                putExtra("Estado Civil", getSpinnerValue(R.id.spinnerEstadoCivil))
                putExtra("Nacionalidade", getSpinnerValue(R.id.spinnerNacionalidade))
                putExtra("Possui Alergias", getSpinnerValue(R.id.spinnerAlergias))
                putExtra("Tem Doença Crônica", getSpinnerValue(R.id.spinnerDoencasCronicas))
                putExtra("Vacinado", getSpinnerValue(R.id.spinnerVacinado))
                putExtra("Fuma", getSpinnerValue(R.id.spinnerFuma))
                putExtra("Bebidas Alcoólicas", getSpinnerValue(R.id.spinnerBebidasAlcoolicas))
                putExtra("Alergia a Remédios", getSpinnerValue(R.id.spinnerAlergiaRemedios))
                putExtra("Atividades Físicas", getSpinnerValue(R.id.spinnerAtividadesFisicas))
                putExtra("Tatuagem Recente", getSpinnerValue(R.id.spinnerTatuagemRecente))
            }
            startActivity(intent)
        } else {
            Toast.makeText(
                this,
                "Você não está elegível para doar sangue no momento",
                Toast.LENGTH_LONG
            ).show()
            finish()
        }
    }

    private fun getSpinnerValue(id: Int): String {
        return findViewById<Spinner>(id).selectedItem.toString()
    }
}
