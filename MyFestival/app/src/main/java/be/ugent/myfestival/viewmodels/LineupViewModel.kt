package be.ugent.myfestival.viewmodels

import androidx.lifecycle.*
import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.models.Stage


class LineupViewModel(private val festivalRepo : FestivalRepository) : ViewModel() {

    //todo: lineup moet ook currentStages beinvloeden
    //dit is MutableLiveData<List<LineupDay>>
    var lineup = festivalRepo.getLineup()

    var currentDay : MutableLiveData<Int> = MutableLiveData(0)

    //todo moet juist geinitialiseerd worden en aangepast worden
    var nextDayClickable = MutableLiveData(true)
    var previousDayClickable = MutableLiveData(true)


    fun getCurrentStages() : LiveData<List<Stage>> =  Transformations.map(currentDay) { day ->
        val lineupDay = lineup.value?.get(day)
        lineupDay?.stages?:emptyList<Stage>()
    }


    fun getCurrentDayString(): LiveData<String> = Transformations.map(currentDay) { day ->
        val lineupDay = lineup.value?.get(day)
        lineupDay?.day?:"today"
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