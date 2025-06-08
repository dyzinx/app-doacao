package com.example.doevida

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SelecaoHospitalActivityTest {

    @Test
    fun testUIElements() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(context, SelecaoHospitalActivity::class.java).apply {
            putExtra("modoTeste", true) // ← evita pedir permissão no teste
            putExtra("tipoSanguineo", "A+")
            putExtra("genero", "Masculino")
            putExtra("estadoCivil", "Solteiro")
            putExtra("nacionalidade", "Brasileira")
            putExtra("alergias", "Não")
            putExtra("doencasCronicas", "Não")
            putExtra("vacinado", "Sim")
            putExtra("fuma", "Não")
            putExtra("bebidasAlcoolicas", "Não")
            putExtra("alergiaRemedios", "Não")
            putExtra("atividadesFisicas", "Frequente")
            putExtra("tatuagemRecente", "Não")
        }

        // Inicia a activity
        ActivityScenario.launch<SelecaoHospitalActivity>(intent)

        // Aguarda renderização da UI
        Thread.sleep(2000)

        // Verifica os elementos visíveis
        onView(withId(R.id.edtSearchLocation)).check(matches(isDisplayed()))
        onView(withId(R.id.btnSearch)).check(matches(isDisplayed()))
        onView(withId(R.id.map)).check(matches(isDisplayed()))
        onView(withId(R.id.hospitalsList)).check(matches(isDisplayed()))
    }
}
