package be.ugent.myfestival.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.models.Stage
import java.time.LocalDate

class LineupViewModel(private val festivalRepo : FestivalRepository) : ViewModel() {

    var currentDay: MutableLiveData<LocalDate> = MutableLiveData()

    //zodat wanneer we op de lineup zitten, geen extra knoppen worden toegevoegd als een concert wordt toegevoegd/verwijderd
    var buttonsSet = false

    fun getAllDaysSorted() : LiveData<List<LocalDate>> = Transformations.map(festivalRepo.getLineup()) {stages ->
        val concerts = stages.flatMap{ it.concerts }
        concerts.map{it.start.toLocalDate()}.distinct().sorted()
    }

    fun setButtons(bool: Boolean) {
        buttonsSet = bool
    }

    fun getDaysMap(listSize: Int) : Map<String, String>{
        if (listSize <= 4) {
            return mapOf(
                "MONDAY" to "Maandag",
                "TUESDAY" to "Dinsdag",
                "WEDNESDAY" to "Woensdag",
                "THURSDAY" to "Donderdag",
                "FRIDAY" to "Vrijdag",
                "SATURDAY" to "Zaterdag",
                "SUNDAY" to "Zondag"
            )
        }
        else {
            return mapOf(
                "MONDAY" to "Ma",
                "TUESDAY" to "Di",
                "WEDNESDAY" to "Wo",
                "THURSDAY" to "Do",
                "FRIDAY" to "Vr",
                "SATURDAY" to "Za",
                "SUNDAY" to "Zo")
        }
    }

    /*getStages en getCurrentStages kunnen eigenlijk in 1 functie, maar we kiezen om deze appart te laten staan zodat we ze allebei
   grondig kunnen testen
    */
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

    fun getCurrentStages() : LiveData<List<Stage>> =  Transformations.switchMap(currentDay) { day ->
        getStages(day)
    }

    fun clickedDay(day: LocalDate) {
        if (currentDay.value !== day) {
            currentDay.postValue(day)
        }
    }
}