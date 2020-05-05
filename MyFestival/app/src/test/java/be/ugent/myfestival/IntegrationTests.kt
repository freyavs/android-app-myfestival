package be.ugent.myfestival

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.MutableLiveData
import be.ugent.myfestival.adapters.DayAdapter
import be.ugent.myfestival.adapters.FestivalChooserAdapter
import be.ugent.myfestival.adapters.FoodStandAdapter
import be.ugent.myfestival.adapters.NewsfeedAdapter
import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.models.*
import be.ugent.myfestival.viewmodels.FestivalChooserViewModel
import be.ugent.myfestival.viewmodels.FestivalViewModel
import be.ugent.myfestival.viewmodels.LineupViewModel
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month


class IntegrationTests {
    @get:Rule
    @Suppress("unused")
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: FestivalRepository
    private lateinit var viewModel: FestivalViewModel

    @Before
    fun setup() {
        repository = mock()
        viewModel = FestivalViewModel(repository)
    }

    @Test
    fun whenNewsfeedItemAddedNewsFeedAdapterUpdates() {
        val items = MutableLiveData(mutableListOf<NewsfeedItem>(mock(), mock()))

        whenever(repository.getNewsfeedItems()).thenReturn(items)

        val newsfeedAdapter = NewsfeedAdapter()

        viewModel.getNewsfeedItems().observeForever { posts ->
            newsfeedAdapter.posts = posts
        }

        Assert.assertEquals(2, newsfeedAdapter.itemCount)
        items.postValue(mutableListOf(mock(), mock(), mock()))
        Assert.assertEquals(3, newsfeedAdapter.itemCount)
    }

    @Test
    fun whenFoodstandAddedFoodstandAdapterUpdates() {
        val foodstands = MutableLiveData(listOf<FoodStand>(mock(), mock()))

        whenever(repository.getFoodstandList()).thenReturn(foodstands)

        val foodStandAdapter = FoodStandAdapter(mock())

        viewModel.getFoodstandList().observeForever { items ->
            foodStandAdapter.foodstands = items
        }

        Assert.assertEquals(2, foodStandAdapter.itemCount)
        foodstands.postValue(mutableListOf(mock(), mock(), mock()))
        Assert.assertEquals(3, foodStandAdapter.itemCount)
    }

    @Test
    fun whenFestivalAddedFestivalChooserAdapterUpdates() {
        val festivalChooserViewModel = FestivalChooserViewModel(repository)
        val festivals = MutableLiveData(listOf<FestivalChooser>(FestivalChooser("1", "a", "")))

        whenever(repository.getFestivals()).thenReturn(festivals)

        val festivalChooserAdapter = FestivalChooserAdapter(mock())
        festivalChooserViewModel.getFestivals().observeForever { items ->
            festivalChooserAdapter.festivalList = items
        }

        Assert.assertEquals(1, festivalChooserAdapter.itemCount)
        festivals.postValue(listOf(FestivalChooser("1", "a", ""), FestivalChooser("2", "a", "")))
        Assert.assertEquals(2, festivalChooserAdapter.itemCount)
    }

    @Test
    fun whenStageremovedDayAdapterUpdates() {
        val concert1 = Concert("c1", "a1", LocalDateTime.of(2020, Month.APRIL, 26, 3,15),
            LocalDateTime.of(2020, Month.APRIL, 27, 3,20))
        val concert2 = Concert("c2", "a2", LocalDateTime.of(2020, Month.APRIL, 26, 3,15),
            LocalDateTime.of(2020, Month.APRIL, 27, 3,20))

        val lineup = MutableLiveData(listOf(Stage("1", "aaa", listOf(concert1, concert2)), Stage("2", "bbb", listOf(concert1, concert2))))

        whenever(repository.getLineup()).thenReturn(lineup)

        val lineupViewModel = LineupViewModel(repository)
        lineupViewModel.clickedDay(LocalDate.of(2020, Month.APRIL, 26))

        val dayAdapter = spy(DayAdapter(mock()))
        doNothing().`when`(dayAdapter).notifyDataSetChanged()

        lineupViewModel.getCurrentStages().observeForever { stages ->
            dayAdapter.notifyChange(stages)
        }

        Assert.assertEquals(2, dayAdapter.count)
        lineup.postValue(listOf(Stage("1", "aaa", listOf(concert1, concert2))))
        Assert.assertEquals(1, dayAdapter.count)
    }

    @Test
    fun whenFestivalAddedItIsFindable(){
        val festivalChooserViewModel = FestivalChooserViewModel(repository)
        val festivals = MutableLiveData(listOf(FestivalChooser("1", "abc", ""), FestivalChooser("2", "abf", "")))

        whenever(repository.getFestivals()).thenReturn(festivals)

        val festivalChooserAdapter = FestivalChooserAdapter(mock())
        festivalChooserViewModel.getFestivals().observeForever { items ->
            festivalChooserAdapter.festivalList = items
        }

        Assert.assertEquals(2, festivalChooserAdapter.itemCount)
        festivalChooserViewModel.changeSearchValue("abc")
        Assert.assertEquals(1, festivalChooserAdapter.itemCount)

        festivals.postValue(listOf(FestivalChooser("1", "abc", ""),
            FestivalChooser("2", "abf", ""),
            FestivalChooser("3", "abc", "")))
        Assert.assertEquals(2, festivalChooserAdapter.itemCount)
        festivalChooserViewModel.changeSearchValue("ab")
        Assert.assertEquals(3, festivalChooserAdapter.itemCount)
    }
}


