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


class FestivalViewModelUnitTests {
    @get:Rule
    @Suppress("unused")
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: FestivalRepository

    private lateinit var preferences: SharedPreferences

    private lateinit var viewModel: FestivalViewModel

    private val testId1 = "123abc"
    private val testId2 = "456def"

    @Before
    fun setup() {
        repository = mock()
        preferences = mock()
        viewModel = FestivalViewModel(repository)

        whenever(preferences.getString("ID",""))
            .thenReturn(testId1)

        whenever(repository.getId()).thenReturn(testId1)
    }


    @Test
    fun setIdCallsResetAndSetId_whenIdIsNotSet() {
        whenever(repository.getId()).thenReturn("")
        viewModel.setId(preferences)
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
        viewModel.setId(preferences)
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

        verify(mockObserver).onChanged("TestFest")
    }

    @Test
    fun getCoordsFromAFestival(){
        val co = listOf(50.455650,3.545465)
        whenever(repository.getCoordsFestival()).thenReturn(MutableLiveData(co))
        val mockObserver = mock<Observer<List<Double>>>()
        viewModel.getCoordsFestival().observeForever(mockObserver)

        verify(mockObserver).onChanged(co)
    }

    @Test
    fun getCoordsFromFoodstands(){
        val foodstands = HashMap<String, List<Double>>()
        foodstands.put("foodstand1", listOf(3.4545,50.545))
        foodstands.put("foodstand2", listOf(3.4546,50.546))
        whenever(repository.getStageCoords(false)).thenReturn(MutableLiveData(foodstands))
        val mockObserver = mock<Observer<HashMap<String, List<Double>>>>()
        viewModel.getFoodstandCoord().observeForever(mockObserver)
        verify(mockObserver).onChanged(foodstands)
    }

    @Test
    fun getCoordsFromStages(){
        val stages = HashMap<String, List<Double>>()
        stages.put("stage1", listOf(3.4545,50.545))
        stages.put("stage2", listOf(3.4546,50.546))
        whenever(repository.getStageCoords(true)).thenReturn(MutableLiveData(stages))
        val mockObserver = mock<Observer<HashMap<String, List<Double>>>>()
        viewModel.getStageCoord().observeForever(mockObserver)
        verify(mockObserver).onChanged(stages)
    }

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

