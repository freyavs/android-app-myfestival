package be.ugent.myfestival.viewmodels

import androidx.lifecycle.ViewModel
import be.ugent.myfestival.data.FestivalRepository

class FestivalChooserViewModel(private val festivalRepo : FestivalRepository) : ViewModel() {
    fun getFestivals() = festivalRepo.getFestivals();
}
