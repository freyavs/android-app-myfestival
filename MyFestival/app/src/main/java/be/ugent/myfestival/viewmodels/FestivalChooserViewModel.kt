package be.ugent.myfestival.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.models.FestivalChooser

class FestivalChooserViewModel(private val festivalRepo : FestivalRepository) : ViewModel() {
    var searchValue: MutableLiveData<String> = MutableLiveData("")
    fun getFestivals() : LiveData<List<FestivalChooser>> = Transformations.switchMap(searchValue) { search ->
        Transformations.map(getSearchedFestivals(search)){
                festivals -> festivals
        }
    }

    fun getSearchedFestivals(search: String) : LiveData<List<FestivalChooser>> = Transformations.map(festivalRepo.getFestivals()) { festivals ->
        val list = mutableListOf<FestivalChooser>()
        for(festival in festivals){
            if(festival.name.toLowerCase().contains(search.toLowerCase()))
                list.add(festival)
        }
        list
    }

    fun changeSearchValue(value: String){
        if(searchValue.value!! !== value){
            searchValue.postValue(value);
        }
    }
}
