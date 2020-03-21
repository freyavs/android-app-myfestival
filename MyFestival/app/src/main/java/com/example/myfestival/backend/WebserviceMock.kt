package com.example.myfestival.backend

import java.util.*

//TODO: deze klasse moet duidelijk vervangen worden door een die de echte info van de database afhaalt,
data class Festival(
    val name: String, val location: String, val lastUpdated: Date)

class WebserviceMock {

    fun fetchFestivalNames() : List<String> {
        //todo voor deel 2
        return emptyList()
    }

    fun fetchFestival(festivalName: String) = Festival(
        festivalName, "Leuven", Date()
    )

}