package com.example.myfestival.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myfestival.data.FestivalRepository

class LineupViewModelFactory(private val repository: FestivalRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LineupViewModel(repository) as T
    }

}