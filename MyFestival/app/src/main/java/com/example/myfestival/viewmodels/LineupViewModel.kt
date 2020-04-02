package com.example.myfestival.viewmodels

import androidx.lifecycle.*
import com.example.myfestival.data.FestivalRepository
import com.example.myfestival.models.Lineup
import com.example.myfestival.models.LineupDay
import com.example.myfestival.models.Stage


class LineupViewModel(private val festivalRepo : FestivalRepository) : ViewModel() {

    //dit is MutableLiveData<List<LineupDay>>
    var lineup = festivalRepo.getLineup()

    //moet eig mutablelivedata zijn
    var currentDay = 0

    //todo moet juist geinitialiseerd worden
    var nextDayClickable = MutableLiveData(true)
    var previousDayClickable = MutableLiveData(true)


    fun getCurrentStages() : LiveData<List<Stage>> =  Transformations.map(lineup) { value ->
        value[currentDay].stages
    }


    fun getCurrentDayString(): LiveData<String> = Transformations.map(lineup) {
            value -> value[currentDay].day
    }

    fun nextDayClicked() {
        //todo logica hier die current day update & clickables update kan wss door map
        if (currentDay > 0) {
            currentDay--
        }

    }

    fun previousDayClicked(){
        //todo logica hier, currentday kleiner dan lineup size
        if (currentDay < 10){
            currentDay++
        }
    }



}