package be.ugent.myfestival.utilities

import android.R
import android.net.Uri
import be.ugent.myfestival.BuildConfig
import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.viewmodels.FestivalViewModelFactory
import be.ugent.myfestival.viewmodels.LineupViewModelFactory
import com.google.firebase.database.FirebaseDatabase


object InjectorUtils {

    //todo: db moet je eigenlijk niet meegeven
    private val db = FirebaseDatabase.getInstance()

    private fun getFestivalRepository(database: FirebaseDatabase) =
        FestivalRepository.getInstance(database)

    fun provideFestivalViewModelFactory() =
        FestivalViewModelFactory(
            getFestivalRepository(
                db
            )
        )

    fun provideLineupViewModelFactory() =
        LineupViewModelFactory(
            getFestivalRepository(
                db
            )
        )
}