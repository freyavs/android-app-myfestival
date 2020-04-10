package be.ugent.myfestival.viewmodels

import androidx.lifecycle.*
import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.models.Stage
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


class LineupViewModel(private val festivalRepo : FestivalRepository) : ViewModel() {

    //todo: lineup moet ook currentStages beinvloeden
    var lineup = festivalRepo.getLineup()

    //todo: currentDay eig weg als we met tabs werken en gwn opvragen via getStages("day")
    var currentDay : MutableLiveData<Int> = MutableLiveData(0)

    //TODO: dit mss gebruiken voor swipen tussen stages of gwn wegdoen?
    var nextDayClickable = MutableLiveData(true)
    var previousDayClickable = MutableLiveData(true)


    //TODO: gebruik dit voor tabs in te vullen van de lineup (geeft gesorteerde lijst van dagen dus bv "23/04, 24/04, 25/04")
    fun getAllDaysSorted() : LiveData<List<Date>> = Transformations.map(lineup) {stages ->
        val concerts = stages.flatMap{ it.concerts }
        concerts.map{ Date.from(it.start.atZone(ZoneId.systemDefault()).toInstant()) }.distinct().sorted()

       // zo zet je de dates om naar strings van de dagen: uniqueDates.map{SimpleDateFormat("EEEE").format(it)}
    }

    //voor als we  "vandaag" ipv "donderdag" willen zetten ofzo maar nog niet echt overnagedacht
    fun getToday(): String = SimpleDateFormat("EEEE").format(LocalDate.now())

    fun getStages(day: Date): LiveData<List<Stage>> = Transformations.map(lineup) { stages ->
        //todo filter voor elke stage lijst van concerts op basis van datum
        listOf<Stage>()
    }


    //TODO: deze functies hieronder refactoren of eig gewoon wegdoen en functies hierboven gebruiken

    fun getCurrentStages() : LiveData<List<Stage>> =  Transformations.map(currentDay) { day ->
        val lineupDay = lineup.value?.get(day)
        emptyList<Stage>()
    }


    fun getCurrentDayString(): LiveData<String> = Transformations.map(currentDay) { day ->
        val lineupDay = lineup.value?.get(day)
        "today"
    }


    fun nextDayClicked() {
        if (currentDay.value!! < 2 ) {
            currentDay.postValue(1)
        }

    }

    fun previousDayClicked(){
        if (currentDay.value!! > 0){
            currentDay.postValue(0)
        }
    }



}