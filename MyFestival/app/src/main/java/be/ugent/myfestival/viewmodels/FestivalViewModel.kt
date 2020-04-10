package be.ugent.myfestival.viewmodels


import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.models.Dish


class FestivalViewModel(private val festivalRepo : FestivalRepository) : ViewModel() {

    fun getWelcomeString(): LiveData<String> = Transformations.map(festivalRepo.getFestivalName()) {
            value -> "Welkom bij $value"
    }

    fun setId(sharedPreferences: SharedPreferences?){
        festivalRepo.festivalID = sharedPreferences?.getString("ID","").toString()
    }

    fun hasFestival(): Boolean{

        Log.v("IDsetter", festivalRepo.festivalID)
        return festivalRepo.festivalID != "Null"
    }

    fun getLineup() = festivalRepo.getLineup()

    fun getFoodstandList() = festivalRepo.getFoodstandList()

    fun getFoodstandMenu(id: String) : LiveData<List<Dish>> =  Transformations.map(festivalRepo.getFoodstandList()) {
            foodstands -> foodstands.filter{ it.id == id }[0].menu
    }

    fun getTestString(): LiveData<String> = Transformations.map(festivalRepo.test) {
            _ -> "Pls werk: halla"
    }

    fun getNewsfeedItems() = festivalRepo.getNewsfeedItems()



}