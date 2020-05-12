package be.ugent.myfestival.data

import androidx.lifecycle.MutableLiveData
import be.ugent.myfestival.models.FestivalChooser
import be.ugent.myfestival.models.FoodStand
import be.ugent.myfestival.models.NewsfeedItem
import be.ugent.myfestival.models.Stage
import com.google.firebase.storage.StorageReference

interface FestivalRepositoryInterface {
    // -------------------------- als id wordt gezet --------------------------
    fun reset(oldId: String)

    fun removeListeners(oldId: String)

    fun initiateData()

    fun getId(): String

    fun setId(id : String)

    // ------------- data voor het festival algemeen (home menu, ..)  ----------------

    fun getFestivalName(): MutableLiveData<String>

    fun getFestivalLogo(): MutableLiveData<StorageReference>

    // ------------- data voor het kaart  -------------------------------------------

    fun getCoordsFestival(): MutableLiveData<List<Double>>

    fun getStageCoords(stage: Boolean): MutableLiveData<HashMap<String, List<Double>>>

    // ---------------- data voor de foodstands -------------------------------

    fun getFoodstandList() : MutableLiveData<List<FoodStand>>

    // ---------------------- data voor de newsfeed -------------------------------

    fun getNewsfeedItems(): MutableLiveData<MutableList<NewsfeedItem>>

    // -------------------------- data voor de lineup ------------------------

    fun getLineup() : MutableLiveData<List<Stage>>

    // -------------- data voor festival chooser ------------------
    fun getFestivals(): MutableLiveData<List<FestivalChooser>>
}