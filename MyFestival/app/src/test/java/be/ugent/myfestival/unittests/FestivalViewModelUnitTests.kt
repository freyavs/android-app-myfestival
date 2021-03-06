package be.ugent.myfestival.unittests


import android.content.SharedPreferences
import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.models.Dish
import be.ugent.myfestival.models.FoodStand
import be.ugent.myfestival.models.Stage
import be.ugent.myfestival.viewmodels.FestivalViewModel
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.invocation.InvocationOnMock


class FestivalViewModelUnitTests {
    @get:Rule
    @Suppress("unused")
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: FestivalRepository

    private lateinit var preferences: SharedPreferences

    private lateinit var viewModel: FestivalViewModel

    var menu1 = listOf(
        Dish("frietjes", "2", false, false),
        Dish("stoofvleessaus", "2", false, false),
        Dish("frikandel", "2", false, false)
    )
    var menu2 = listOf(
        Dish("hamburger", "2", false, false),
        Dish("cheeseburger", "2", false, false),
        Dish("ribburger", "2", false, false)
    )

    var foodstand1 = FoodStand("12321", "Frietjes bij Pol", mock(), menu1)
    var foodstand2 = FoodStand("32123", "Burger Boys", mock(), menu2)
    val foodstands = listOf(foodstand1,foodstand2)

    private val testId1 = "123abc"
    private val testId2 = "456def"

    @Before
    fun setup() {
        repository = mock()
        preferences = mock()
        viewModel = FestivalViewModel(repository)

        whenever(preferences.getString("ID",""))
            .thenReturn(testId1)

        whenever(repository.getFoodstandList())
            .thenReturn(MutableLiveData(foodstands))

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
    fun removeIdSetsFestivalIdEmptyAndRemovesListenersWithOldId(){
        repository.festivalID = testId1
        viewModel.removeId()
        verify(repository).setId("")
        verify(repository).removeListeners(testId1)
    }

    @Test
    fun hasFestivalReturnsTrue_whenFestivalIdIsSet(){
        Assert.assertTrue(viewModel.hasFestival())
    }

    @Test
    fun welcomeStringIsFormattedWhenNameIsNotEmpty(){
        whenever(repository.getFestivalName()).thenReturn( MutableLiveData("TestFest"))
        val mockObserver = mock<Observer<String>>()
        viewModel.getWelcomeString().observeForever(mockObserver)

        verify(mockObserver).onChanged("Welkom bij TestFest")
    }

    @Test
    fun welcomeStringGivesInternetErrorWhenNameIsEmpty(){
        whenever(repository.getFestivalName()).thenReturn( MutableLiveData(""))
        val mockObserver = mock<Observer<String>>()
        viewModel.getWelcomeString().observeForever(mockObserver)

        verify(mockObserver).onChanged("Geen internetverbinding...")
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
    fun loadingSwitchesToVisibleWhenNameIsNotEmpty(){
        whenever(repository.name).thenReturn(MutableLiveData())
        val mockObserver = mock<Observer<Int>>()
        viewModel.getLoading().observeForever(mockObserver)

        repository.name.apply { postValue("name") }

        verify(mockObserver).onChanged(View.VISIBLE)
    }

    @Test
    fun correctFoodstandReturnedOnGetFoodstand() {
        val mockObserver = mock<Observer<FoodStand>>()
        viewModel.getFoodstand("12321").observeForever(mockObserver)

        verify(mockObserver).onChanged(foodstand1)
    }

    @Test
    fun loadingSwitchesToInisibleWhenNameIsEmpty(){
        whenever(repository.name).thenReturn(MutableLiveData())
        val mockObserver = mock<Observer<Int>>()
        viewModel.getLoading().observeForever(mockObserver)

        repository.name.apply { postValue("") }

        verify(mockObserver).onChanged(View.INVISIBLE)
    }
}

