package com.example.doevida

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuestionarioActivityTest {

    @Test
    fun testBotaoConcluirExiste_naoExplode() {
        ActivityScenario.launch(QuestionarioActivity::class.java)

        // Verifica se o botão existe e tem visibilidade VISIBLE, mesmo que fora da tela
        onView(withId(R.id.btnConcluir))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }
}
