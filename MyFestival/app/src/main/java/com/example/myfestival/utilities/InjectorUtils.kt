package com.example.myfestival.utilities

import com.example.myfestival.data.FestivalRepository
import com.example.myfestival.viewmodels.FestivalViewModelFactory
import com.example.myfestival.viewmodels.LineupViewModelFactory
import com.google.firebase.database.FirebaseDatabase

object InjectorUtils {

    //todo: db moet je eigenlijk niet meegeven
    private val db = FirebaseDatabase.getInstance()

    private fun getFestivalRepository(database: FirebaseDatabase) =
        FestivalRepository.getInstance(database)

    fun provideFestivalViewModelFactory() =
        FestivalViewModelFactory(getFestivalRepository(db))

    fun provideLineupViewModelFactory() =
        LineupViewModelFactory(getFestivalRepository(db))
}