package be.ugent.myfestival.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.models.Stage
import java.time.LocalDate

class LineupViewModel(private val festivalRepo : FestivalRepository) : ViewModel() {

    var currentDay: MutableLiveData<LocalDate> = MutableLiveData(LocalDate.now())

    fun getAllDaysSorted() : LiveData<List<LocalDate>> = Transformations.map(festivalRepo.getLineup()) {stages ->
        val concerts = stages.flatMap{ it.concerts }
        concerts.map{it.start.toLocalDate()}.distinct().sorted()
    }

    fun getStages(day: LocalDate): LiveData<List<Stage>> = Transformations.map(festivalRepo.getLineup()) { stages ->
        val list = mutableListOf<Stage>()
        for (stage in stages){
            var concerts = stage.concerts.filter{it.start.toLocalDate().isEqual(day)}
            if (concerts.isNotEmpty()){
                concerts = concerts.sortedBy { concert -> concert.start }
                list.add(Stage(stage.id, stage.name, concerts))
            }
        }
        list
    }

    fun getToday(): LocalDate = LocalDate.now()

    fun clickedDay(day: LocalDate) {
        if (currentDay.value !== day) {
            currentDay.postValue(day)
        }
    }

    fun getCurrentStages() : LiveData<List<Stage>> =  Transformations.switchMap(currentDay) { day ->
        getStages(day)
    }
}