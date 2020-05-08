package be.ugent.myfestival

import android.content.SharedPreferences
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.models.Concert
import be.ugent.myfestival.models.Stage
import be.ugent.myfestival.viewmodels.FestivalViewModel
import be.ugent.myfestival.viewmodels.LineupViewModel
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

// tests = 6

class LineupViewModelUnitTests {
    @get:Rule
    @Suppress("unused")
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    var currentDay: LocalDate = LocalDate.now()

    var concert1stage1 = Concert("c1", "a1", LocalDateTime.of(2020, Month.APRIL, 26, 3,15),
        LocalDateTime.of(2020, Month.APRIL, 27, 3,20))

    var concert1stage2 = Concert("c2", "a2", LocalDateTime.of(2020, Month.APRIL, 28, 3,15),
        LocalDateTime.of(2020, Month.APRIL, 28, 3,20))

    var concert2stage2 = Concert("c3", "a3", LocalDateTime.of(2020, Month.APRIL, 29, 3,15),
        LocalDateTime.of(2020, Month.APRIL, 29, 3,20))

    var concert1stage3 = Concert("c4", "a4", LocalDateTime.of(2020, Month.APRIL, 28, 3,15),
        LocalDateTime.of(2020, Month.APRIL, 28, 3,20))

    var concert2stage3 = Concert("c5", "a5", LocalDateTime.of(2020, Month.APRIL, 29, 3,15),
     LocalDateTime.of(2020, Month.APRIL, 29, 3,20))

    var stage1 = Stage("1", "s1", listOf(concert1stage1))

    var stage2 = Stage("2", "s2", listOf(concert1stage2, concert2stage2))

    var stage3 = Stage("3", "s3", listOf(concert1stage3, concert2stage3))

    var lineup: List<Stage> = listOf(stage3, stage1, stage2)

    private lateinit var repository: FestivalRepository

    private lateinit var lineupViewModel: LineupViewModel


    @Before
    fun setup() {
        repository = mock()

        whenever(repository.getLineup())
            .thenReturn(MutableLiveData(lineup))

        lineupViewModel = LineupViewModel(repository)
    }

    @Test
    fun getDaysSorted_returnsSorted() {
        //linup is ongesorteerd
        val sortedDays = listOf(LocalDate.of(2020, Month.APRIL, 26), LocalDate.of(2020, Month.APRIL, 28), LocalDate.of(2020, Month.APRIL, 29))
        val mockObserver = mock<Observer<List<LocalDate>>>()
        lineupViewModel.getAllDaysSorted().observeForever(mockObserver)

        verify(mockObserver).onChanged(sortedDays)
    }

    @Test
    fun dateChangesCorrectly(){
        val mockObserver = mock<Observer<LocalDate>>()
        lineupViewModel.currentDay.observeForever(mockObserver)
        lineupViewModel.clickedDay(LocalDate.of(2020, Month.APRIL, 29))

        verify(mockObserver).onChanged(LocalDate.of(2020, Month.APRIL, 29))
    }

    @Test
    fun dateDoesNotChangeWhenNotInFestival(){
        val mockObserver = mock<Observer<LocalDate>>()
        lineupViewModel.currentDay.observeForever(mockObserver)
        lineupViewModel.clickedDay(LocalDate.of(2020, Month.APRIL, 25))

        verify(mockObserver).onChanged(currentDay)
    }

    @Test
    fun noStagesReturnedWhenDateNotInFestival(){
        lineupViewModel.currentDay.apply { postValue(
            LocalDate.of(2020, Month.APRIL, 20)) }
        val mockObserver = mock<Observer<List<Stage>>>()
        lineupViewModel.getStages(LocalDate.of(2020, Month.APRIL, 20)).observeForever(mockObserver)

        verify(mockObserver).onChanged(listOf())

    }

    @Test
    fun correctStagesReturnedWhenDateInFestival(){
        val mockObserver = mock<Observer<List<Stage>>>()
        lineupViewModel.getStages(LocalDate.of(2020, Month.APRIL, 26)).observeForever(mockObserver)

        lineupViewModel.currentDay.apply { postValue(
            LocalDate.of(2020, Month.APRIL, 26)) }

        //hier zou er een manier nodig zijn om de lijsten te comparen op id, dus een transformatie van de observer?
        verify(mockObserver).onChanged(argForWhich {
            this.size == 1 && stage1.id  == this[0].id
        })
    }

    @Test
    fun stagesChangeWhenDateChanges() {
        val mockObserverDate = mock<Observer<LocalDate>>()
        lineupViewModel.currentDay.observeForever(mockObserverDate)

        val mockObserver = mock<Observer<List<Stage>>>()
        lineupViewModel.getCurrentStages().observeForever(mockObserver)

        val day = LocalDate.of(2020, Month.APRIL, 26);
        lineupViewModel.clickedDay(day)
        //currentday wordt aangepast maar de stages worden niet aangepast in de observer
        verify(mockObserver).onChanged(listOf())
        verify(mockObserver, atLeastOnce()).onChanged(
            argForWhich {
                this.size == 1 && stage1.id  == this[0].id
            }
        ) }
}