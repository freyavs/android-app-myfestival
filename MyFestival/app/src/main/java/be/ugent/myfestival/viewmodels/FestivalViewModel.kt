package be.ugent.myfestival.viewmodels


import android.content.SharedPreferences
import android.transition.Transition
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.*
import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.models.Dish
import be.ugent.myfestival.models.NewsfeedItem
import be.ugent.myfestival.utilities.GlideApp


class FestivalViewModel(private val festivalRepo : FestivalRepository) : ViewModel() {

    fun getFestivals() = festivalRepo.getFestivals();

    fun getWelcomeString(): LiveData<String> =
        Transformations.map(festivalRepo.getFestivalName()) { value ->
            "Welkom bij $value"
        }

    fun setId(sharedPreferences: SharedPreferences?){
        val newID = sharedPreferences?.getString("ID","Null").toString()
        if (newID != festivalRepo.festivalID) {
            festivalRepo.reset()
            festivalRepo.festivalID = sharedPreferences?.getString("ID", "Null").toString()
            getLineup()
            getFoodstandList()
            getNewsfeedItems()
            getMap()
        }
    }

    fun hasFestival(): Boolean{
        return festivalRepo.festivalID != "Null"
    }

    fun getLineup() = festivalRepo.getLineup()

    fun getLogo() = festivalRepo.getFestivalLogo()

    fun getMap() = festivalRepo.getFestivalMap()

    fun getFoodstandList() = festivalRepo.getFoodstandList()

    fun getFoodstandMenu(id: String): LiveData<List<Dish>> =
        Transformations.map(festivalRepo.getFoodstandList()) { foodstands ->
            foodstands.filter { it.id == id }[0].menu
        }

    fun getNewsfeedItems() = festivalRepo.getNewsfeedItems()


    //TODO: Loading moet beter / mooier met afbeelding ofzo

    fun getLoading() : LiveData<Int> = Transformations.map(festivalRepo.lineupstages){ value ->
        Log.v("welcome_string", value.isEmpty().toString())
        Log.v("welcome_string", value.toString())
        when(value.isNullOrEmpty()) {
            true -> View.INVISIBLE
            else -> View.VISIBLE
        }
    }

    fun getNotLoading() : LiveData<Int> = Transformations.map(festivalRepo.lineupstages){ value ->
        Log.v("welcome_string", value.isEmpty().toString())
        Log.v("welcome_string", value.toString())
        when(value.isNullOrEmpty()) {
            true -> View.VISIBLE
            else -> View.INVISIBLE
        }
    }


    companion object {
        //databinding met glide
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: ImageView, url: String?) {
            GlideApp.with(view.context).load(url).into(view)
        }

    }
}