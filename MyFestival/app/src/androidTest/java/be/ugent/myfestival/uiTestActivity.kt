package be.ugent.myfestival

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import be.ugent.myfestival.adapters.FestivalChooserAdapter
import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.data.FestivalRepositoryInterface
import be.ugent.myfestival.models.FestivalChooser
import be.ugent.myfestival.utilities.InjectorUtils
import be.ugent.myfestival.viewmodels.FestivalViewModel
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.mock.declareMock
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class uiTestActivity : KoinTest {

    @Test
    fun chooseOrSwitchFestival(){
        ActivityScenario.launch(MainActivity::class.java)
        //indien de app al een keer is gebruikt is er een id set. daarom kan het zijn dat we reeds op een festival uitkomen
        //hiervoor dient de trycatch: indien op festival => keer eerst terug
        //dit is natuurlijk een beetje een hack
        try {
            onView(withId(R.id.festival_recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            );
        } catch (e: NoMatchingViewException) {
            onView(withId(R.id.festival_chooser_btn)).perform(click())
            onView(withId(R.id.festival_recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )
        }
    }
    @Test
    fun newsfeedButtonIsClickable(){
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.newsfeed_btn)).perform(click())
    }
    @Test
    fun lineupButtonIsClickable(){
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.lineup_btn)).perform(click())
    }
    @Test
    fun switchDayInFestivalLineup(){
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.lineup_btn)).perform(click())
        onView(withText("Zaterdag")).perform(click())
    }
    @Test
    fun foodButtonIsClickable(){
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.food_btn)).perform(click())
    }
    @Test
    fun menuItemsOpenMenu(){
        ActivityScenario.launch(MainActivity::class.java)
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
        onView(withId(R.id.map_btn)).perform(click())
    }

    @Test
    fun z_festivalChooserpButtonIsClickable(){
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.festival_chooser_btn)).perform(click())
    }

}