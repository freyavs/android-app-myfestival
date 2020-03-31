package com.example.myfestival.utilities

import com.example.myfestival.data.FestivalRepository
import com.example.myfestival.viewmodels.FestivalViewModelFactory
import com.google.firebase.database.FirebaseDatabase

object InjectorUtils {
    private val db = FirebaseDatabase.getInstance()

    private fun getFestivalRepository(database: FirebaseDatabase) =
        FestivalRepository.getInstance(database)

    fun provideFestivalViewModelFactory() =
        FestivalViewModelFactory(getFestivalRepository(db))
}