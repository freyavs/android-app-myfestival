package be.ugent.myfestival.viewmodels


import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.*
import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.models.Dish
import be.ugent.myfestival.utilities.GlideApp


class FestivalViewModel(private val festivalRepo : FestivalRepository) : ViewModel() {

    fun getWelcomeString(): LiveData<String> =
        Transformations.map(festivalRepo.getFestivalName()) { value ->
            "Welkom bij $value"
        }

    fun getLineup() = festivalRepo.getLineup()

    fun getLogo() = festivalRepo.getFestivalLogo()

    fun getFoodstandList() = festivalRepo.getFoodstandList()

    fun getFoodstandMenu(id: String): LiveData<List<Dish>> =
        Transformations.map(festivalRepo.getFoodstandList()) { foodstands ->
            foodstands.filter { it.id == id }[0].menu
        }

    fun getNewsfeedItems() = festivalRepo.getNewsfeedItems()

    companion object {
        //databinding met glide
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: ImageView, url: String?) {
            GlideApp.with(view.context).load(url).into(view)
        }

    }
}