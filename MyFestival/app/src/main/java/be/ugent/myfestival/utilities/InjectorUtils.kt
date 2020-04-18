package be.ugent.myfestival.utilities

import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.viewmodels.FestivalViewModelFactory
import be.ugent.myfestival.viewmodels.LineupViewModelFactory
import com.google.firebase.database.FirebaseDatabase


object InjectorUtils {

    private fun getFestivalRepository() =
        FestivalRepository.getInstance()

    fun provideFestivalViewModelFactory() =
        FestivalViewModelFactory(
            getFestivalRepository()
        )

    fun provideLineupViewModelFactory() =
        LineupViewModelFactory(
            getFestivalRepository()
        )


}