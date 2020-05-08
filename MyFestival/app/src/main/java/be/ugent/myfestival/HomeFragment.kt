package be.ugent.myfestival

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import be.ugent.myfestival.databinding.HomeFragmentBinding
import be.ugent.myfestival.notifications.BackgroundNotificationService.Companion.TAG
import be.ugent.myfestival.utilities.GlideApp
import be.ugent.myfestival.utilities.InjectorUtils
import be.ugent.myfestival.viewmodels.FestivalViewModel
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey
import java.io.File

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = HomeFragmentBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        context?: return binding.root

        val viewModel by activityViewModels<FestivalViewModel> {
            InjectorUtils.provideFestivalViewModelFactory()
        }


        viewModel.setId(context?.getSharedPreferences("FestivalPreference", Context.MODE_PRIVATE), context)

        //kijken of er een ID is, zo niet gaat het naar het festival kies scherm
        if(!viewModel.hasFestival()){
            val action = HomeFragmentDirections.actionHomeFragmentToFestivalChooserFragment()
            findNavController().navigate(action)
        }

        binding.viewModel = viewModel

        //speciale methode om het logo te tonen zodat zo weinig mogelijk de afbeelding moet worden opgehaald van de firebase storage
        viewModel.getLogo().observe( this, Observer { logoRef ->
            GlideApp.with(context!!)
                .load(logoRef)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(binding.logoView)

            //TODO: controleer of offline ook werkt als logo verandert is, anders moet het als volgt:
           /*
            val localFile = File(context?.filesDir,"logo.jpeg")
            val logoRefId = logoRef?.toString()?.split("/")?.last()
            Log.d("myFestivalTag", "logo ref id: " + logoRef)
            val preference = context?.getSharedPreferences("FestivalLogo", Context.MODE_PRIVATE)
            val prevId = preference?.getString("ID","").toString()
            //als vorige logoRef niet hetzelfde was (dus logo of festival is veranderd) of nog niet bestond dan laadt opnieuw image
            if (!localFile.exists() || logoRefId != prevId ){
                logoRef.getFile(localFile).addOnSuccessListener {
                    Log.d("myFestivalTag", "Tempfile created for logo of festival.")
                    loadLogo(localFile.absolutePath, binding)
                    val editor = preference?.edit()
                    editor?.putString("ID", logoRefId)
                    editor?.apply()
                }.addOnFailureListener {
                    Log.d("myFestivalTag", "Tempfile failed: check if festival submitted a logo!")
                }
            }
            else {
                Log.d("myFestivalTag", "LOGO: logo already exists, loading logo.jpeg")
                loadLogo(localFile.absolutePath, binding)
            }*/
        })
        
        binding.newsfeedHandler = View.OnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToNewsfeedFragment()
            findNavController().navigate(action)
        }

        binding.foodHandler = View.OnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToFoodFragment()
            findNavController().navigate(action)
        }

        binding.lineupHandler = View.OnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToLineupFragment()
            findNavController().navigate(action)
        }

        binding.mapHandler = View.OnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToMapsFragment()
            findNavController().navigate(action)
        }
        binding.festivalChooserHandler = View.OnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToFestivalChooserFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }

    fun loadLogo(filepath: String, binding: HomeFragmentBinding){
        GlideApp.with(context!!)
            .load(filepath)
            .signature(ObjectKey(System.currentTimeMillis().toString()))
            .into(binding.logoView)
    }
}
