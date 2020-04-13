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

    var lineup = festivalRepo.getLineup()

    val TAG = "myFestivalTag"


    //TODO: als 'vandaag' tussen beschikbare dagen zit, zorg dan dat het start op vandaag, anders op de 1ste dag
    var currentDay: MutableLiveData<LocalDate> = MutableLiveData()

    fun getAllDaysSorted() : LiveData<List<LocalDate>> = Transformations.map(lineup) {stages ->
        Log.d(TAG, "getting sorted days")
        val concerts = stages.flatMap{ it.concerts }
        concerts.map{it.start.toLocalDate()}.distinct().sorted()
    }

    fun getStages(day: LocalDate): LiveData<List<Stage>> = Transformations.map(lineup) { stages ->
        val list = mutableListOf<Stage>()
        for (stage in stages){
            var concerts = stage.concerts.filter{it.start.toLocalDate() == day}
            concerts = concerts.sortedBy { concert -> concert.start }
            list.add(Stage(stage.name, concerts))
        }
        list
    }

    fun clickedDay(day: LocalDate) {
        currentDay.postValue(day)
    }

    fun getCurrentStages() : LiveData<List<Stage>> =  Transformations.map(currentDay) { day ->
        Log.d(TAG, "GET CURRENTS STAGES: " + day.toString())
        getStages(day).value!!
    }


}