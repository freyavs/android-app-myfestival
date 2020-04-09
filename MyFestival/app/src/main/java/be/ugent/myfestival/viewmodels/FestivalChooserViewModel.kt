package be.ugent.myfestival.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import be.ugent.myfestival.data.FestivalRepository

class FestivalChooserViewModel(private val festivalRepo : FestivalRepository) : ViewModel() {
    fun getTestString(): LiveData<String> = Transformations.map(festivalRepo.test) {
        value -> "Pls werkt: halla"
    }
}
