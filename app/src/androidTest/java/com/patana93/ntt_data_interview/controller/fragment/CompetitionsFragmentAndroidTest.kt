package com.patana93.ntt_data_interview.controller.fragment

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.google.common.truth.Truth.assertThat
import com.patana93.ntt_data_interview.R
import com.patana93.ntt_data_interview.controller.adapter.CompetitionsRecyclerViewAdapter
import kotlinx.coroutines.delay
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CompetitionsFragmentAndroidTest{

    lateinit var fragmentScenario: FragmentScenario<CompetitionsFragment>

    @Before
    fun setup(){
        fragmentScenario = launchFragmentInContainer()
    }

    @Test
    fun testVisibilityCompetitionsRecycler() {
        onView(withId(R.id.competitionsRecyclerView)).check(matches(isDisplayed()))
    }

    @Test
    fun testActionToTeamMostWinFragmentNavigation() {
        // Create a TestNavHostController
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext())

        // Create a graphical FragmentScenario for the TitleScreen
        val titleScenario = launchFragmentInContainer<CompetitionsFragment>()

        titleScenario.onFragment { fragment ->
            // Set the graph on the TestNavHostController
            navController.setGraph(R.navigation.main_navigation)

            // Make the NavController available via the findNavController() APIs
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        // Verify that performing a click changes the NavController’s state
        onView(withId(R.id.competitionCardView)).perform(click())
        assertThat(navController.currentDestination?.id).isEqualTo(R.id.teamsMostWinFragment)
    }
}