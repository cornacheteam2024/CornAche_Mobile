package com.example.cornache

import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        //IdlingRegistry.getInstance().register(CustomEspressoIdlingResources.countingIdlingResource)
    }

    @After
    fun tearDown() {
        //IdlingRegistry.getInstance().unregister(CustomEspressoIdlingResources.countingIdlingResource)
    }

    @Test
    fun testIfLoginSuccess() {
        Espresso.onView(withId(R.id.username))
            .perform(ViewActions.typeText("testing"), ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(R.id.password))
            .perform(ViewActions.typeText("password"), ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(R.id.loginButton))
            .perform(ViewActions.click())
        Espresso.onView(withText(R.string.dialog_continue))
            .perform(ViewActions.click())
    }
}