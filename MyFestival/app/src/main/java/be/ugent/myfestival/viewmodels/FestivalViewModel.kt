package be.ugent.myfestival.viewmodels


import androidx.lifecycle.*
import be.ugent.myfestival.data.FestivalRepository


class FestivalViewModel(private val festivalRepo : FestivalRepository) : ViewModel() {

    fun getWelcomeString(): LiveData<String> = Transformations.map(festivalRepo.getFestivalName()) {
            value -> "Welkom bij het $value"
    }

    //TODO dit is gwn copy paste van de repository er moet hier nog iets anders gebeuren
    //TODO moet sws ook met festivalID
    fun getLineup() = festivalRepo.getLineup()

    fun getFoodstandList() = festivalRepo.getFoodstandList()

    fun getNewsfeedItems() = festivalRepo.getNewsfeedItems()

    fun getFoodstandMenu(id: String) = festivalRepo.getFoodstandMenu(id)


}