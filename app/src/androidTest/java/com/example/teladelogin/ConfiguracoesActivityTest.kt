package com.example.teladelogin

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConfiguracoesActivityTest {

    @get:Rule
    val rule = ActivityScenarioRule(ConfiguracoesActivity::class.java)

    @Test
    fun testConfiguracoesUIElements() {
        onView(withId(R.id.iconeMenu)).check(matches(isDisplayed()))
        onView(withId(R.id.logoCentral)).check(matches(isDisplayed()))
        onView(withId(R.id.iconeUsuario)).check(matches(isDisplayed()))

        onView(withId(R.id.switchNotificacoes)).check(matches(isDisplayed()))
        onView(withId(R.id.switchModoEscuro)).check(matches(isDisplayed()))
        onView(withId(R.id.btnLimparCache)).check(matches(isDisplayed()))
        onView(withId(R.id.btnRedefinirPreferencias)).check(matches(isDisplayed()))
        onView(withId(R.id.btnPoliticaPrivacidade)).check(matches(isDisplayed()))
    }
}
