package be.ugent.utilities

import android.content.Context
import com.example.myfestival.data.FestivalDatabase
import com.example.myfestival.data.FestivalRepository
import com.example.myfestival.viewmodels.FestivalViewModelFactory

object InjectorUtils {

    private fun getForecastRepository(context: Context) =
        FestivalRepository.getInstance(
            FestivalDatabase.getInstance(context).festivalDao()
        )

    fun provideForecastViewModelFactory(context: Context) =
        FestivalViewModelFactory(getForecastRepository(context))

}