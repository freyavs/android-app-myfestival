package com.example.myfestival.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myfestival.data.FestivalRepository
import com.google.firebase.database.FirebaseDatabase

class FestivalViewModelFactory( private val repository: FestivalRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FestivalViewModel(repository) as T
    }

}