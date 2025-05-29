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
class EditarPerfilActivityTest {

    @get:Rule
    val rule = ActivityScenarioRule(EditarPerfilActivity::class.java)

    @Test
    fun testUIElements() {
        onView(withId(R.id.imagePerfil)).check(matches(isDisplayed()))
        onView(withId(R.id.ic_camera)).check(matches(isDisplayed()))
        onView(withId(R.id.edtNome)).check(matches(isDisplayed()))
        onView(withId(R.id.edtEmail)).check(matches(isDisplayed()))
        onView(withId(R.id.edtCelular)).check(matches(isDisplayed()))
        onView(withId(R.id.btnAlterarSenha)).check(matches(isDisplayed()))
        onView(withId(R.id.btnSalvar)).check(matches(isDisplayed()))
    }
}
