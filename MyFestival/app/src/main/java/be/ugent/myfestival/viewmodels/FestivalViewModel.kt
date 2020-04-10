package be.ugent.myfestival.viewmodels


import android.util.Log
import androidx.lifecycle.*
import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.models.Dish


class FestivalViewModel(private val festivalRepo : FestivalRepository) : ViewModel() {

    fun getWelcomeString(): LiveData<String> = Transformations.map(festivalRepo.getFestivalName()) {
            value -> "Welkom bij $value"
    }

    fun getLineup() = festivalRepo.getLineup()

    fun getLogo() = festivalRepo.getFestivalLogo()

    fun getFoodstandList() = festivalRepo.getFoodstandList()

    fun getFoodstandMenu(id: String) : LiveData<List<Dish>> =  Transformations.map(festivalRepo.getFoodstandList()) {
            foodstands -> foodstands.filter{ it.id == id }[0].menu
    }

    fun getTestString(): LiveData<String> = Transformations.map(festivalRepo.test) {
            _ -> "Pls werk: halla"
    }

    fun getNewsfeedItems() = festivalRepo.getNewsfeedItems()



}