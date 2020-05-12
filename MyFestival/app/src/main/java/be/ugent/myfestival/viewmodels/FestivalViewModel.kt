package be.ugent.myfestival.viewmodels

import android.content.SharedPreferences
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.models.FoodStand


class  FestivalViewModel(private val festivalRepo : FestivalRepository) : ViewModel() {

    fun getWelcomeString(): LiveData<String> =
        Transformations.map(festivalRepo.getFestivalName()) { value ->
            if (value == ""){
                "Geen internetverbinding..."
            }
            else {
                "Welkom bij $value"
            }
        }

    fun getFestivalName() = festivalRepo.getFestivalName()

    fun getCurrentFestivalId() = festivalRepo.getId()

    fun removeId(){
        //bij reset worden pas de waarden op null gezet
        val oldId = festivalRepo.getId()
        festivalRepo.setId("")
        festivalRepo.removeListeners(oldId)
    }

    fun setId(sharedPreferences: SharedPreferences?){
        val newID = sharedPreferences?.getString("ID","").toString()
        if (newID != festivalRepo.getId()) {
            val oldId = festivalRepo.getId()
            festivalRepo.setId(newID)
            festivalRepo.reset(oldId)
        }
    }

    fun removeListeners() = festivalRepo.removeListeners(festivalRepo.getId())

    fun hasFestival(): Boolean{
        return festivalRepo.getId() != ""
    }

    fun getLogo() = festivalRepo.getFestivalLogo()

    fun getFoodstandList(): MutableLiveData<List<FoodStand>> {
        return festivalRepo.getFoodstandList()
    }

    fun getFoodstand(id: String): LiveData<FoodStand> =
        Transformations.map(festivalRepo.getFoodstandList()) { foodstands ->
            foodstands.filter { it.id == id }[0]
        }


    fun getNewsfeedItems() = festivalRepo.getNewsfeedItems()

    fun getNewMessageTitle(): MutableLiveData<String> = festivalRepo.newMessageTitle

    /*
    dankzij firebase zal bij het inladen van de festivals, alles in de cache worden ingeladen waardoor (ook offline)
    deze functie enkel nuttig zal zijn in randgevallen waar bv het laden van een festival werd stopgezet.
    Het is de bedoeling dat de 4 knoppen dan niet zichtbaar zijn (waarvoor deze functie) aangezien er toch enkel lege data is,
    er zal worden aangegeven dat er internet verbinding moet zijn.
     */
    fun getLoading() : LiveData<Int> = Transformations.map(festivalRepo.name){ value ->
        when(value.isNullOrEmpty()) {
            true -> View.INVISIBLE
            else -> View.VISIBLE
        }
    }

    fun getCoordsFestival(): MutableLiveData<List<Double>> = festivalRepo.getCoordsFestival()

    fun getStageCoord(): MutableLiveData<HashMap<String,List<Double>>> = festivalRepo.getStageCoords(true)

    fun getFoodstandCoord(): MutableLiveData<HashMap<String,List<Double>>> = festivalRepo.getStageCoords(false)
}
