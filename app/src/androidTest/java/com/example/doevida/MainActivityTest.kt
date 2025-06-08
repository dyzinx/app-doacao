package com.example.doevida

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun verificaTodosOsElementosVisiveisENaoCrasham() {
        // Campo de e-mail
        onView(withId(R.id.et_email))
            .check(matches(isDisplayed()))
            .check(matches(withHint("E-mail")))

        // Campo de senha
        onView(withId(R.id.et_senha))
            .check(matches(isDisplayed()))
            .check(matches(withHint("Senha")))

        // Botão Login
        onView(withId(R.id.btn_login))
            .check(matches(isDisplayed()))
            .check(matches(withText("Login")))
            .check(matches(isClickable()))

        // Botão Cadastrar-se
        onView(withId(R.id.btn_cadastro))
            .check(matches(isDisplayed()))
            .check(matches(withText("Cadastrar-se")))
            .check(matches(isClickable()))
    }
}
