package com.example.myfestival.viewmodels

import androidx.lifecycle.*
import com.example.myfestival.data.FestivalRepository


class LineupViewModel(private val festivalRepo : FestivalRepository) : ViewModel() {
    var name: MutableLiveData<String> = MutableLiveData()

    fun getDayString(): LiveData<String> = Transformations.map(festivalRepo.getFestivalName()) {
            value -> "$value"
    }

    fun getLineup() = festivalRepo.getLineup()



}