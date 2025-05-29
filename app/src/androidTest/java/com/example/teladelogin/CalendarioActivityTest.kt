package com.example.teladelogin

import android.widget.CalendarView
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CalendarioActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(CalendarioActivity::class.java)

    @Test
    fun testCalendarioUIElements() {
        // Verifica se o CalendarView está visível e é do tipo correto
        onView(allOf(
            withId(R.id.calendarView),
            isAssignableFrom(CalendarView::class.java)
        )).check(matches(isDisplayed()))

        // Verifica se o botão "Escolher dia" está visível e com o texto correto
        onView(withId(R.id.btnEscolherDia))
            .check(matches(isDisplayed()))
            .check(matches(withText("Escolher dia")))
    }
}
