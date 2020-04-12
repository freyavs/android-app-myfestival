package be.ugent.myfestival.viewmodels

import android.util.Log
import androidx.lifecycle.*
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
    var currentDay: MutableLiveData<Date> = MutableLiveData()

    fun getAllDaysSorted() : LiveData<List<Date>> = Transformations.map(lineup) {stages ->
        Log.d(TAG, "getting sorted days")
        val concerts = stages.flatMap{ it.concerts }
        concerts.map{ Date.from(it.start.atZone(ZoneId.systemDefault()).toInstant()) }.distinct().sorted()
       // zo zet je de dates om naar strings van de dagen: uniqueDates.map{SimpleDateFormat("EEEE").format(it)}
    }

    //voor als we  "vandaag" ipv "donderdag" willen zetten ofzo maar nog niet echt overnagedacht
    fun getToday(): String = SimpleDateFormat("EEEE").format(LocalDate.now())

    fun getStages(day: Date): LiveData<List<Stage>> = Transformations.map(lineup) { stages ->
        val list = mutableListOf<Stage>()
        for (stage in stages){
            var concerts = stage.concerts.filter{Date.from(it.start.atZone(ZoneId.systemDefault()).toInstant()) == day}
            concerts = concerts.sortedBy { concert -> concert.start }
            list.add(Stage(stage.name, concerts))
        }
        list
    }

    fun clickedDay(day: Date) {
        currentDay.postValue(day)
    }

    //todo: moet nog beter dan met getalldayssorted...
    fun getCurrentStages() : LiveData<List<Stage>> =  Transformations.map(currentDay) { day ->
        Log.d(TAG, "GET CURRENTS STAGES: " + day.toString())
        getStages(day).value!!
    }


}