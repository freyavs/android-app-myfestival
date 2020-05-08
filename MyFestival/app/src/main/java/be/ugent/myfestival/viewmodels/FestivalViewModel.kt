package be.ugent.myfestival.viewmodels


import android.content.Context
import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.models.Dish
import be.ugent.myfestival.models.FoodStand
import java.io.File


class  FestivalViewModel(private val festivalRepo : FestivalRepository) : ViewModel() {

    fun getWelcomeString(): LiveData<String> =
        Transformations.map(festivalRepo.getFestivalName()) { value ->
            "$value"
        }

    fun getFestivalName() = festivalRepo.getFestivalName()

    fun getCurrentFestivalId() = festivalRepo.getId()

    fun setId(sharedPreferences: SharedPreferences?, context: Context?){
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

    fun getMap() = festivalRepo.getFestivalMap()

    fun getFoodstandList(): MutableLiveData<List<FoodStand>> {
        return festivalRepo.getFoodstandList()
    }

    //TODO: getfoodstandmenu weg en wisseln voor getFoodstand (ook in tests)
    fun getFoodstandMenu(id: String): LiveData<List<Dish>> =
        Transformations.map(festivalRepo.getFoodstandList()) { foodstands ->
            foodstands.filter { it.id == id }[0].menu
        }

    fun getFoodstand(id: String): LiveData<FoodStand> =
        Transformations.map(festivalRepo.getFoodstandList()) { foodstands ->
            foodstands.filter { it.id == id }[0]
        }


    fun getNewsfeedItems() = festivalRepo.getNewsfeedItems()

    fun getNewMessageTitle(): MutableLiveData<String> = festivalRepo.newMessageTitle

    //TODO: Loading moet beter / mooier met afbeelding ofzo en buttons mogen ook niet op scherm verschijnen (gwn loading icon/afb die over heel het scherm is)

    fun getLoading() : LiveData<Int> = Transformations.map(festivalRepo.lineupstages){ value ->
        when(value.isNullOrEmpty()) {
            true -> View.INVISIBLE
            else -> View.VISIBLE
        }
    }

    fun getNotLoading() : LiveData<Int> = Transformations.map(festivalRepo.lineupstages){ value ->
        when(value.isNullOrEmpty()) {
            true -> View.GONE
            else -> View.INVISIBLE
        }
    }

    fun getReady(): LiveData<Int> = Transformations.map(festivalRepo.getFestivalName()){ value ->
        when(value.isNullOrEmpty()) {
            false -> View.GONE
            else -> View.VISIBLE
        }
    }

    fun getCoordsFestival(): MutableLiveData<List<Double>> = festivalRepo.getCoordsFestival()

    fun getStageCoord(): MutableLiveData<HashMap<String,List<Double>>> = festivalRepo.getStageCoords()

}
