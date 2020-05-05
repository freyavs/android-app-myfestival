package be.ugent.myfestival

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import be.ugent.myfestival.adapters.FestivalChooserAdapter
import be.ugent.myfestival.adapters.FoodStandAdapter
import be.ugent.myfestival.adapters.NewsfeedAdapter
import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.models.FestivalChooser
import be.ugent.myfestival.models.FoodStand
import be.ugent.myfestival.models.NewsfeedItem
import be.ugent.myfestival.viewmodels.FestivalChooserViewModel
import be.ugent.myfestival.viewmodels.FestivalViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

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
}


