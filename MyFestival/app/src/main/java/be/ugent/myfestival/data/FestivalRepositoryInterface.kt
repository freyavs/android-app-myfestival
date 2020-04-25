package be.ugent.myfestival.data

import androidx.lifecycle.MutableLiveData
import be.ugent.myfestival.models.*
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class FestivalRepositoryInterface() {

    //Todo: weghalen bij integratie
    private val db = FirebaseDatabase.getInstance()
    private val storage = Firebase.storage.reference
    private fun getFestivalRepository(database: FirebaseDatabase, storage: StorageReference) =
        FestivalRepository.getInstance(database,storage)
    val repository = getFestivalRepository(db, storage)
    //Todo: einde weghalen

    // -------------------------- als id wordt gezet --------------------------
    fun reset() = repository.reset()

    fun getId() = repository.reset()

    fun setId(id : String) = repository.setId(id)

    // ------------- data voor het festival algemeen (home menu, ..)  ----------------

    fun getFestivalName(): MutableLiveData<String> = repository.getFestivalName()

    fun getFestivalLogo(): MutableLiveData<String> = repository.getFestivalLogo()

    fun getFestivalMap(): MutableLiveData<String> = repository.getFestivalMap()

    // ---------------- data voor de foodstands -------------------------------
    fun getFoodstandList() : MutableLiveData<List<FoodStand>> = repository.getFoodstandList()


    // ---------------------- data voor de newsfeed -------------------------------


    fun getNewsfeedItems(): MutableLiveData<MutableList<NewsfeedItem>> = repository.getNewsfeedItems()

    // -------------------------- data voor de lineup ------------------------

    fun getLineup() : MutableLiveData<List<Stage>> = repository.getLineup()

    // -------------- data voor festival chooser ------------------
    fun getFestivals(): MutableLiveData<List<FestivalChooser>> = repository.getFestivals()
}