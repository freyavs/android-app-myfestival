package be.ugent.myfestival.utilities

import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.viewmodels.FestivalViewModelFactory
import be.ugent.myfestival.viewmodels.LineupViewModelFactory
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage


object InjectorUtils {

    private val db = FirebaseDatabase.getInstance()
    private val storage = Firebase.storage.reference

    private fun getFestivalRepository(database: FirebaseDatabase, storage: StorageReference) =
        FestivalRepository.getInstance(database,storage)

    fun provideFestivalViewModelFactory() =
        FestivalViewModelFactory(
            getFestivalRepository(
                db,storage
            )
        )

    fun provideLineupViewModelFactory() =
        LineupViewModelFactory(
            getFestivalRepository(
                db,storage
            )
        )
}