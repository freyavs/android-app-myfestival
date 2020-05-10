package be.ugent.myfestival

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.data.FestivalRepositoryInterface
import be.ugent.myfestival.utilities.InjectorUtils
import be.ugent.myfestival.viewmodels.FestivalViewModel
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.mock.declareMock
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class uiTestActivity : KoinTest {

    val repositoryInstance: FestivalRepositoryInterface = InjectorUtils.provideRepository()
    private var repository = Mockito.spy(repositoryInstance)
    @Before
    fun setUp() {
        whenever(repository.getId()).thenReturn("-M3b9hJNsFaCXAi8Gegq")
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
    fun festivalChooserpButtonIsClickable(){
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.festival_chooser_btn)).perform(click())
    }

}