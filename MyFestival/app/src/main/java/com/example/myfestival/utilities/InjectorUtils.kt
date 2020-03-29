package be.ugent.utilities

import android.content.Context
import com.example.myfestival.viewmodels.FestivalViewModelFactory

object InjectorUtils {

    fun provideForecastViewModelFactory() =
        FestivalViewModelFactory()

}

//TODO: eig is deze klasse niet nodig? tenzij je terug met repository begint te werken