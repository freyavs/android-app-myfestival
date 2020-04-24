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
import be.ugent.myfestival.models.FestivalChooser
import be.ugent.myfestival.models.FoodStand
import be.ugent.myfestival.models.NewsfeedItem
import be.ugent.myfestival.utilities.GlideApp


class FestivalViewModel(private val festivalRepo : FestivalRepository) : ViewModel() {
    var festivalList: MutableLiveData<List<FestivalChooser>> = MutableLiveData()
    fun getFestivals(zoekwaarde: String): MutableLiveData<List<FestivalChooser>> {
        val allFestivals = festivalRepo.getFestivals()
        val festivalChoosers = mutableListOf<FestivalChooser>()
        allFestivals.value?.forEach {
            Log.v("kakapipi", it.toString())
            if(it.name.toLowerCase().contains(zoekwaarde.toLowerCase())) {
                festivalChoosers.add(it)
            }
        }
        festivalList.postValue(festivalChoosers)
        return festivalList
    }

    fun getWelcomeString(): LiveData<String> =
        Transformations.map(festivalRepo.getFestivalName()) { value ->
            "Welkom bij $value"
        }

    fun setId(sharedPreferences: SharedPreferences?){
        val newID = sharedPreferences?.getString("ID","").toString()
        if (newID != festivalRepo.getId()) {
            festivalRepo.setId(sharedPreferences?.getString("ID", "").toString())
            festivalRepo.reset()
        }
    }

    fun hasFestival(): Boolean{
        return festivalRepo.getId() != ""
    }

    fun getLogo() = festivalRepo.getFestivalLogo()

    fun getMap() = festivalRepo.getFestivalMap()

    fun getFoodstandList(): MutableLiveData<List<FoodStand>> {
        val TAG = "myFestivalTag"
        Log.w(TAG, "getfoodstands")
        return festivalRepo.getFoodstandList()
    }
    val foodstands = festivalRepo.getFoodstandList()

    fun getFoodstandMenu(id: String): LiveData<List<Dish>> =
        Transformations.map(festivalRepo.getFoodstandList()) { foodstands ->
            foodstands.filter { it.id == id }[0].menu
        }

    fun getNewsfeedItems() = festivalRepo.getNewsfeedItems()


    //TODO: Loading moet beter / mooier met afbeelding ofzo en buttons mogen ook niet op scherm verschijnen (gwn loading icon/afb die over heel het scherm is)

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