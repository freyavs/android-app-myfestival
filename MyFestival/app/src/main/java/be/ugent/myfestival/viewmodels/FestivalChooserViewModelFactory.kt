package be.ugent.myfestival.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.ugent.myfestival.data.FestivalRepository

class FestivalChooserViewModelFactory(private val repository: FestivalRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FestivalChooserViewModel(repository) as T
    }

}