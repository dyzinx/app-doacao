package com.example.teladelogin

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CadastroActivityTest {

    @get:Rule
    val rule = ActivityScenarioRule(CadastroActivity::class.java)

    @Test
    fun testCadastroUIElements() {
        onView(withId(R.id.editNome)).check(matches(isDisplayed()))
        onView(withId(R.id.editSobrenome)).check(matches(isDisplayed()))
        onView(withId(R.id.editTelefone)).check(matches(isDisplayed()))
        onView(withId(R.id.editConfirmarSenha)).check(matches(isDisplayed()))
        onView(withId(R.id.btnCadastrar)).check(matches(isDisplayed()))
    }
}
