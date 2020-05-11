package be.ugent.myfestival

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.koin.test.KoinTest


@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class uiTestActivity : KoinTest {

    @Before
    fun setUp() {
        //indien de app al eens geopend is en er een festival geselecteerd werd dan staat dit festival in de preferences
        //en zal de test vanop een ander fragment beginnen, wij willen dus steeds dat de test begint vanop het keuzescherm
        val targetContext : Context = getInstrumentation().targetContext
        val preference = targetContext.getSharedPreferences("FestivalPreference", Context.MODE_PRIVATE)
        val editor = preference?.edit()
        editor?.putString("ID","")
        editor?.apply()
    }

    @Test
    fun chooseFestival(){
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.festival_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

    }
    @Test
    fun newsfeedButtonIsClickable(){
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.festival_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.newsfeed_btn)).perform(click())
    }
    @Test
    fun lineupButtonIsClickable(){
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.festival_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.lineup_btn)).perform(click())
    }
    @Test
    fun switchDayInFestivalLineup(){
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.festival_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                click()
            )
        )
        onView(withId(R.id.lineup_btn)).perform(click())
        onView(withText("Zaterdag")).perform(click())
    }
    @Test
    fun foodButtonIsClickable(){
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.festival_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.food_btn)).perform(click())
    }
    @Test
    fun menuItemsOpenMenu(){
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.festival_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.food_btn)).perform(click())
        onView(withId(R.id.foodstand_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
    }
    @Test
    fun mapButtonIsClickable(){
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.festival_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.map_btn)).perform(click())
    }

    @Test
    fun returnToFestivalChooser(){
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.festival_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.festival_chooser_btn)).perform(click())
    }

}