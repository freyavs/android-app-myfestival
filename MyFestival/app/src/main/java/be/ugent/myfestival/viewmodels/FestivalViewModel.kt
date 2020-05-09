package be.ugent.myfestival.viewmodels

import android.content.SharedPreferences
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
            "$value"
        }

    fun getFestivalName() = festivalRepo.getFestivalName()

    fun getCurrentFestivalId() = festivalRepo.getId()

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

    fun getLoading() : LiveData<Int> = Transformations.map(festivalRepo.lineupstages){ value ->
        when(value.isNullOrEmpty()) {
            true -> View.INVISIBLE
            else -> View.VISIBLE
        }
    }

    fun getCoordsFestival(): MutableLiveData<List<Double>> = festivalRepo.getCoordsFestival()

    fun getStageCoord(): MutableLiveData<HashMap<String,List<Double>>> = festivalRepo.getStageCoords(true)
    fun getFoodstandCoord(): MutableLiveData<HashMap<String,List<Double>>> = festivalRepo.getStageCoords(false)

}
