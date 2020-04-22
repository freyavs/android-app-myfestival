package be.ugent.myfestival.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.models.Stage
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class LineupViewModel(private val festivalRepo : FestivalRepository) : ViewModel() {

    val TAG = "myFestivalTag"

    var currentDay: MutableLiveData<LocalDate> = MutableLiveData(LocalDate.now())


    //TODO: nog aanduiden dat swipen mogelijk is of niet adhv livedata en "<" en ">" mss (en dit ook zeker testen in de unit tests)

    fun getAllDaysSorted() : LiveData<List<LocalDate>> = Transformations.map(festivalRepo.getLineup()) {stages ->
        val concerts = stages.flatMap{ it.concerts }
        concerts.map{it.start.toLocalDate()}.distinct().sorted()
    }

    fun getStages(day: LocalDate): LiveData<List<Stage>> = Transformations.map(festivalRepo.getLineup()) { stages ->
        val list = mutableListOf<Stage>()
        for (stage in stages){
            var concerts = stage.concerts.filter{it.start.toLocalDate() == day}
            concerts = concerts.sortedBy { concert -> concert.start }
            list.add(Stage(stage.id, stage.name, concerts))
        }
        list
    }

    fun getToday() = LocalDate.now()

    fun clickedDay(day: LocalDate) {
        if (currentDay.value!! !== day) {
            currentDay.postValue(day)
        }
    }

    fun getCurrentStages() : LiveData<List<Stage>> =  Transformations.switchMap(currentDay) { day ->
        Log.d(TAG, "Get current stages: " + day.toString())
        Transformations.map(getStages(day)) {
            stages -> stages
        }
    }

}