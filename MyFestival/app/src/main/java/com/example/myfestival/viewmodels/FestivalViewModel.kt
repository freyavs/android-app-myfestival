package com.example.myfestival.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfestival.data.FestivalEntity
import com.example.myfestival.data.FestivalRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class FestivalViewModel(private val repository: FestivalRepository) : ViewModel() {

    private val festival: LiveData<FestivalEntity> by lazy {
        repository.getFestival()
    }

    private val formatter = SimpleDateFormat("HH:mm:ss")

    fun getLocationString(): LiveData<String> = Transformations.map(festival) {
        value -> "${value.location}°"
    }

    fun getWelcomeString(): LiveData<String> = Transformations.map(festival) {
            value -> "Welcome to ${value.name}!"
        }

    fun getLastUpdatedString(): LiveData<String> = Transformations.map(festival) {
        value -> "Last updated: ${formatter.format(value.lastUpdated)}"
    }

    fun update() {
        viewModelScope.launch {
            repository.update()
        }
    }
}