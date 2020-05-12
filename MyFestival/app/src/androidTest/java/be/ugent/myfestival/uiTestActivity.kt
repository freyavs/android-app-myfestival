package be.ugent.myfestival

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.junit.Before
import org.junit.FixMethodOrder
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
        preference.edit().putString("ID","").apply()
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun chooseFestival(){
        onView(withId(R.id.festival_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

    }
    @Test
    fun newsfeedButtonIsClickable(){
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
        onView(withId(R.id.festival_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.festival_chooser_btn)).perform(click())
    }

    @Test
    fun addTextInSearchField() {
        onView(withId(R.id.search_festival)).perform(click()).perform(pressKey(1))
    }

}