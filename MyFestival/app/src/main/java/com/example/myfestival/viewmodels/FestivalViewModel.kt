package com.example.myfestival.viewmodels


import androidx.lifecycle.*
import com.example.myfestival.data.FestivalRepository


class FestivalViewModel(private val festivalRepo : FestivalRepository) : ViewModel() {
    var name: MutableLiveData<String> = MutableLiveData()

    fun getWelcomeString(): LiveData<String> = Transformations.map(festivalRepo.getFestivalName()) {
            value -> "Welcome to $value"
    }


}