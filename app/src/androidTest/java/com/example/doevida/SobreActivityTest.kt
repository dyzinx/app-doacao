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
class SobreActivityTest {

    @get:Rule
    val rule = ActivityScenarioRule(SobreActivity::class.java)

    @Test
    fun testUIElements() {
        onView(withId(R.id.iconeMenu)).check(matches(isDisplayed()))
        onView(withId(R.id.logoCentral)).check(matches(isDisplayed()))
        onView(withId(R.id.iconeUsuario)).check(matches(isDisplayed()))
    }
}
