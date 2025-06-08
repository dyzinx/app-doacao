package com.example.doevida

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConfirmarAgendamentoActivityTest {

    @get:Rule
    val rule = ActivityScenarioRule(ConfirmarAgendamentoActivity::class.java)

    @Test
    fun testUIElements() {
        onView(withId(R.id.imageHospital)).check(matches(isDisplayed()))
        onView(withId(R.id.editLocal)).check(matches(isDisplayed()))
        onView(withId(R.id.editData)).check(matches(isDisplayed()))
        onView(withId(R.id.spinnerHorario)).check(matches(isDisplayed()))
        onView(withId(R.id.btnConfirmar)).check(matches(isDisplayed()))
    }
}
