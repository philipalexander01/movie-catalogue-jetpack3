package com.example.moviecatalogue

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.moviecatalogue.utils.DataDummy
import com.example.moviecatalogue.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class HomeActivityTest {
    private val dataMovie = DataDummy.getDataMovie()
    private val dataTv = DataDummy.getDataTv()

    @get:Rule
    var activityRule = ActivityScenarioRule(HomeActivity::class.java)

    @Before
    fun setup(){
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }

    @Test
    fun loadMovie() {
        onView(withId(R.id.rv_movie)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_movie)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                dataMovie.size
            )
        )
    }

    @Test
    fun loadDetailMovie(){
        onView(withId(R.id.rv_movie)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
            ViewActions.click()
        ))

        onView(withId(R.id.tvTitle)).check(matches(isDisplayed()))

        onView(withId(R.id.tvDate)).check(matches(isDisplayed()))

        onView(withId(R.id.tvOverview)).check(matches(isDisplayed()))
        //insert data to room database
        onView(withId(R.id.favButton)).check(matches(isDisplayed()))
        onView(withId(R.id.favButton)).perform(
            ViewActions.click()
        )
        onView(withId(R.id.ivBack)).perform(ViewActions.click())


        onView(withId(R.id.rv_movie)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1,
                ViewActions.click()
        ))

        onView(withId(R.id.tvTitle)).check(matches(isDisplayed()))

        onView(withId(R.id.tvDate)).check(matches(isDisplayed()))

        onView(withId(R.id.tvOverview)).check(matches(isDisplayed()))
        //insert data to room database
        onView(withId(R.id.favButton)).check(matches(isDisplayed()))
        onView(withId(R.id.favButton)).perform(
                ViewActions.click()
        )
    }


    @Test
    fun loadTvAndDetail(){
        onView(withText("TV SHOW")).perform(ViewActions.click())

        onView(withId(R.id.rv_movie)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_movie)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                dataTv.size
            )
        )

        //add data one
        onView(withId(R.id.rv_movie)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
            ViewActions.click()
        ))

        onView(withId(R.id.tvTitle)).check(matches(isDisplayed()))

        onView(withId(R.id.tvDate)).check(matches(isDisplayed()))

        onView(withId(R.id.tvOverview)).check(matches(isDisplayed()))

        //insert data to room database
        onView(withId(R.id.favButton)).check(matches(isDisplayed()))
        onView(withId(R.id.favButton)).perform(
            ViewActions.click()
        )
        onView(withId(R.id.ivBack)).perform(ViewActions.click())

        //add data two
        onView(withId(R.id.rv_movie)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1,
                ViewActions.click()
        ))

        onView(withId(R.id.tvTitle)).check(matches(isDisplayed()))

        onView(withId(R.id.tvDate)).check(matches(isDisplayed()))

        onView(withId(R.id.tvOverview)).check(matches(isDisplayed()))

        //insert data to room database
        onView(withId(R.id.favButton)).check(matches(isDisplayed()))
        onView(withId(R.id.favButton)).perform(
                ViewActions.click()
        )

    }



    @Test
    fun loadMovieFavAndDetail() {
        onView(withId(R.id.fav)).check(matches(isDisplayed()))
        onView(withId(R.id.fav)).perform(
                ViewActions.click()
        )
        onView(withId(R.id.rv_movie))
                .check(matches(isDisplayed()))
        onView(ViewMatchers.withId(R.id.rv_movie)).perform(
                RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                        0
                )
        )


        onView(ViewMatchers.withId(R.id.rv_movie)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                ViewActions.click()
        ))

        onView(ViewMatchers.withId(R.id.tvTitle))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(ViewMatchers.withId(R.id.tvDate))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(ViewMatchers.withId(R.id.tvOverview))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        //remove data one room database
        onView(withId(R.id.favButton)).check(matches(isDisplayed()))
        onView(withId(R.id.favButton)).perform(
                ViewActions.click()
        )
    }




    @Test
    fun loadTvAndDetailFav(){
        onView(withText("TV SHOW")).perform(ViewActions.click())

        onView(withId(R.id.rv_movie))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.rv_movie)).perform(
                RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                        0
                )
        )

        Espresso.onView(ViewMatchers.withId(R.id.rv_movie)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                ViewActions.click()
        ))

        Espresso.onView(ViewMatchers.withId(R.id.tvTitle))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.tvDate))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.tvOverview))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        //remove data one room database
        onView(withId(R.id.favButton)).check(matches(isDisplayed()))
        onView(withId(R.id.favButton)).perform(
                ViewActions.click()
        )
    }
}