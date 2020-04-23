package be.ugent.myfestival


import android.content.SharedPreferences
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
import org.mockito.Mock

//aantal unit tests = 5

//TODO: test loading dingen maar pas als het is aangepast mss? (zie festivalviewmodel)
//TODO: test foodstand dingen?
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
        viewModel.setId(preferences)
        verify(repository).reset()
        verify(repository).setId(testId1)
    }

    @Test
    fun setIdCallsResetAndSetId_whenIdIsDifferentFromOldId(){
        whenever(preferences.getString("ID",""))
            .thenReturn(testId2)

        verify(repository, never()).reset()
        verify(repository, never()).setId(testId2)
    }

    @Test
    fun setIdDoesNotCallResetAndSetId_whenIdEqualsOldId() {
        viewModel.setId(preferences)
        verify(repository, never()).reset()
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

}

