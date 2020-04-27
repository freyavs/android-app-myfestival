package be.ugent.myfestival.viewmodels


import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.databinding.FestivalChooserFragmentBindingImpl
import com.bumptech.glide.request.target.Target
import be.ugent.myfestival.models.Dish
import be.ugent.myfestival.models.FestivalChooser
import be.ugent.myfestival.models.FoodStand
import be.ugent.myfestival.utilities.GlideApp
import be.ugent.myfestival.utilities.InjectorUtils
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import java.io.File


class FestivalViewModel(private val festivalRepo : FestivalRepository) : ViewModel() {
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
    fun getWelcomeString(): LiveData<String> =
        Transformations.map(festivalRepo.getFestivalName()) { value ->
            "Welkom bij $value"
        }

    fun getFestivalName() = festivalRepo.getFestivalName()
    fun getCurrentFestivalId() = festivalRepo.getId()

    fun setId(sharedPreferences: SharedPreferences?, context: Context?){
        val newID = sharedPreferences?.getString("ID","").toString()
        if (newID != festivalRepo.getId()) {
            //verwijder alle files van vorig festival
            deleteTempFiles(context?.cacheDir!!)
            festivalRepo.setId(sharedPreferences?.getString("ID", "").toString())
            festivalRepo.reset()
        }
    }

    //er is hier een kotlin one-liner voor maar we willen bepaalde files niet verwijderen
    fun deleteTempFiles(file: File): Boolean {
        if (file.isDirectory()) {
            val files: Array<File>? = file.listFiles()
            if (files != null) {
                for (f in files) {
                    if (f.isDirectory()) {
                        deleteTempFiles(f)
                    } else if (!f.absolutePath.contains("festivallist")) {
                        f.delete()
                    }
                }
            }
        }
        return file.delete()
    }

    fun hasFestival(): Boolean{
        return festivalRepo.getId() != ""
    }

    fun getLogo() = festivalRepo.getFestivalLogo()

    fun getMap() = festivalRepo.getFestivalMap()

    fun getFoodstandList(): MutableLiveData<List<FoodStand>> {
        return festivalRepo.getFoodstandList()
    }

    fun getFoodstandMenu(id: String): LiveData<List<Dish>> =
        Transformations.map(festivalRepo.getFoodstandList()) { foodstands ->
            foodstands.filter { it.id == id }[0].menu
        }

    fun getNewsfeedItems() = festivalRepo.getNewsfeedItems()

    fun getNewMessageTitle(): MutableLiveData<String> = festivalRepo.newMessageTitle
    fun resetNewMessageTitle() = festivalRepo.resetNewMessageTitle()


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
            true -> View.GONE
            else -> View.INVISIBLE
        }
    }

    fun getReady(): LiveData<Int> = Transformations.map(festivalRepo.getFestivalName()){ value ->
        when(value.isNullOrEmpty()) {
            false -> View.GONE
            else -> View.VISIBLE
        }
    }

    companion object {
        //databinding met glide
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: ImageView, url: String?) {
            GlideApp.with(view.context).load(url)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        return false
                    }
                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        Log.d("myL", "OnResourceReady")

                        //do something when picture already loaded
                        return false
                    }
                })
                .into(view)

        }

    }
}
