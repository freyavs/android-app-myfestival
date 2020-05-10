package be.ugent.myfestival

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
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

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    val repositoryInstance: FestivalRepositoryInterface = InjectorUtils.provideRepository()
    private var repository = Mockito.spy(repositoryInstance)
    @Before
    fun setUp() {
        //whenever(repository.getId()).thenReturn("-M3b9hJNsFaCXAi8Gegq")
    }
    @Test
    fun aChooseAFestival(){
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.festival_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()));


        //onView(withId(R.id.rv_conference_list)).perform(
        //            RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id. bt_deliver)))
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
    fun foodButtonIsClickable(){
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.food_btn)).perform(click())
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