package com.example.myfestival.viewmodels

import androidx.lifecycle.*
import com.example.myfestival.data.FestivalRepository
import com.example.myfestival.models.Lineup
import com.example.myfestival.models.LineupDay
import com.example.myfestival.models.Stage


class LineupViewModel(private val festivalRepo : FestivalRepository) : ViewModel() {

    //todo: lineup moet ook currentStages beinvloeden
    //dit is MutableLiveData<List<LineupDay>>
    var lineup = festivalRepo.getLineup()

    //moet eig mutablelivedata zijn
    var currentDay = MutableLiveData(0)

    //todo moet juist geinitialiseerd worden en aangepast worden
    var nextDayClickable = MutableLiveData(true)
    var previousDayClickable = MutableLiveData(true)



    //todo: !! is natuurlijk niet de beste methode
    fun getCurrentStages() : LiveData<List<Stage>> =  Transformations.map(currentDay) { day ->
        lineup.value!![day].stages
    }


    fun getCurrentDayString(): LiveData<String> = Transformations.map(currentDay) { d ->
        lineup.value!![d].day
    }

    fun nextDayClicked() {
        if (currentDay.value!! > 0) {
            currentDay.value = 1
        }

    }

    fun previousDayClicked(){
        if (currentDay.value!! < 2){
            currentDay.value = 0
        }
    }



}