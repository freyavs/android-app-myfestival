package com.example.myfestival.data

import android.util.Log
import androidx.lifecycle.liveData
import com.example.myfestival.backend.WebserviceMock
import java.util.*

class FestivalRepository private constructor(private val festivalDao: FestivalDao) {

    private val webservice = WebserviceMock()

    fun getFestival() = liveData {
        emitSource(festivalDao.getFestival())
        Log.d("DEBUGGING", "update was called")
        update()
    }

    fun getFestivalNames() = webservice.fetchFestivalNames();

    suspend fun update() {
        //TODO: zie in ForecastApp hoe
        //todo: deel 2 moet een selectie kunnen gemaakt worden en rock werchter dus niet hardcoderen
        festivalDao.insert(webservice.fetchFestival("Rock werchter"))
    }

    companion object {

        @Volatile private var instance: FestivalRepository? = null

        fun getInstance(forecastDao: FestivalDao) = instance
            ?: synchronized(this) {
            instance
                ?: FestivalRepository(forecastDao).also { instance = it }
        }
    }

}