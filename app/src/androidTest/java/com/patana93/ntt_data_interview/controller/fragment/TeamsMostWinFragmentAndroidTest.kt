package com.patana93.ntt_data_interview.controller.fragment

import androidx.constraintlayout.utils.widget.MockView
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.patana93.ntt_data_interview.R
import org.junit.Before
import org.junit.Test

class TeamsMostWinFragmentAndroidTest{

    lateinit var fragmentScenario: FragmentScenario<TeamsMostWinFragment>

    @Before
    fun setup(){
        fragmentScenario = launchFragmentInContainer()
    }

    @Test
    fun testVisibilityTeamsRecycler() {
        Espresso.onView(ViewMatchers.withId(R.id.teamsRecyclerView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testVisibilityTitle() {
        Espresso.onView(ViewMatchers.withId(R.id.titleMostWinFragTextView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}