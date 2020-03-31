package com.example.myfestival.viewmodels


import androidx.lifecycle.*
import com.example.myfestival.data.FestivalRepository


class FestivalViewModel(private val festivalRepo : FestivalRepository) : ViewModel() {
    var name: MutableLiveData<String> = MutableLiveData()

    fun getWelcomeString(): LiveData<String> = Transformations.map(festivalRepo.getFestivalName()) {
            value -> "Welcome to $value"
    }

    //TODO dit is gwn copy paste van de repository er moet hier nog iets anders gebeuren
    //TODO moet sws ook met festivalID
    fun getLineup() = festivalRepo.getLineup()

    fun getFoodstandList() = festivalRepo.getFoodstandList()

    fun getNewsfeedItems() = festivalRepo.getNewsfeedItems()

    fun getFoodstandMenu(id: String) = festivalRepo.getFoodstandMenu(id)


}