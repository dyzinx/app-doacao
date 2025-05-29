package com.example.teladelogin

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.google.firebase.auth.FirebaseAuth
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HistoricoDoacoesActivityTest {

    private val emailTeste = "testador@example.com"
    private val senhaTeste = "123456"

    @Before
    fun loginUsuarioDeTeste() {
        val latch = java.util.concurrent.CountDownLatch(1)

        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailTeste, senhaTeste)
            .addOnCompleteListener {
                latch.countDown()
            }

        latch.await() // Espera login concluir
    }

    @Test
    fun testUIElementsMesmoSemAgendamentos() {
        ActivityScenario.launch(HistoricoDoacoesActivity::class.java)

        onView(withText("Histórico de Doações")).check(matches(isDisplayed()))
        onView(withText("Doações Agendadas")).check(matches(isDisplayed()))
        onView(withText("Doações Realizadas")).check(matches(isDisplayed()))
    }
}
