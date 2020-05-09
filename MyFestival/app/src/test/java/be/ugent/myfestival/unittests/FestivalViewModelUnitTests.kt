package be.ugent.myfestival.unittests


import android.content.SharedPreferences
import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.models.Stage
import be.ugent.myfestival.viewmodels.FestivalViewModel
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

//aantal unit tests = 8

//TODO: test foodstand dingen
//TODO: test of na reset juiste data wordt ingeladen (alle getters dus)
//denk na welke tests hier moeten en welke in integration test (androidStudio) -> mss eens vragen eig

class FestivalViewModelUnitTests {
    @get:Rule
    @Suppress("unused")
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: FestivalRepository

    private lateinit var preferences: SharedPreferences

    private lateinit var viewModel: FestivalViewModel

    private lateinit var lineupObserver: Observer<List<Stage>>

    private val testId1 = "123abc"
    private val testId2 = "456def"

    @Before
    fun setup() {
        repository = mock()
        preferences = mock()
        viewModel = FestivalViewModel(repository)

        //TODO: dingen testen met observers? zie code H7 of dit uit H8:
        /*@Test
        fun getWishListReturnsReturnsData() {
            val wishes = listOf(Wishlist("Victoria", listOf("RW Book")))
            whenever(mockRepository.getWishlists())
                .thenReturn(MutableLiveData<List<Wishlist>>().apply { postValue(wishes) })

            val mockObserver = mock<Observer<List<Wishlist>>>()
            viewModel.getWishlists().observeForever(mockObserver)

            verify(mockObserver).onChanged(wishes)
        }*/

        //lineupObserver = mock()
        //viewModel.getLineup().observeForever(mapObserver)

        whenever(preferences.getString("ID",""))
            .thenReturn(testId1)

        whenever(repository.getId()).thenReturn(testId1)
    }


    @Test
    fun setIdCallsResetAndSetId_whenIdIsNotSet() {
        whenever(repository.getId()).thenReturn("")
        viewModel.setId(preferences,null)
        verify(repository).reset(any())
        verify(repository).setId(testId1)
    }

    @Test
    fun setIdCallsResetAndSetId_whenIdIsDifferentFromOldId(){
        whenever(preferences.getString("ID",""))
            .thenReturn(testId2)

        verify(repository, never()).reset(any())
        verify(repository, never()).setId(testId2)
    }

    @Test
    fun setIdDoesNotCallResetAndSetId_whenIdEqualsOldId() {
        viewModel.setId(preferences,null)
        verify(repository, never()).reset(any())
        verify(repository, never()).setId(testId1)
    }

    @Test
    fun hasFestivalReturnsFalse_whenFestivalIdIsNotSet(){
        whenever(repository.getId()).thenReturn("")

        Assert.assertFalse(viewModel.hasFestival())
    }

    @Test
    fun hasFestivalReturnsTrue_whenFestivalIdIsSet(){
        Assert.assertTrue(viewModel.hasFestival())
    }

    @Test
    fun welcomeStringIsFormatted(){
        whenever(repository.getFestivalName()).thenReturn( MutableLiveData("TestFest"))
        val mockObserver = mock<Observer<String>>()
        viewModel.getWelcomeString().observeForever(mockObserver)

        verify(mockObserver).onChanged("Welkom bij TestFest")
    }

   /* @Test
    fun newsfeedItemSizeIsNullSafe(){
        whenever(repository.getNewsfeedItems()).thenReturn(MutableLiveData())

        Assert.assertEquals(0, viewModel.getNewsfeedItemsSize())
    }*/

    @Test
    fun loadingSwitchesToVisibleWhenReady(){
        whenever(repository.lineupstages).thenReturn(MutableLiveData())
        val mockObserver = mock<Observer<Int>>()
        viewModel.getLoading().observeForever(mockObserver)

        val stage : Stage = mock()
        repository.lineupstages.apply { postValue(listOf(stage)) }

        verify(mockObserver).onChanged(View.VISIBLE)
    }
}

