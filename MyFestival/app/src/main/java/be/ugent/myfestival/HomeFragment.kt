package be.ugent.myfestival

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import be.ugent.myfestival.databinding.HomeFragmentBinding
import be.ugent.myfestival.utilities.GlideApp
import be.ugent.myfestival.utilities.InjectorUtils
import be.ugent.myfestival.viewmodels.FestivalViewModel
import com.bumptech.glide.load.engine.DiskCacheStrategy

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

        viewModel.setId(context?.getSharedPreferences("FestivalPreference", Context.MODE_PRIVATE))

        //kijken of er een ID is, zo niet gaat het naar het festival kies scherm
        if(!viewModel.hasFestival()){
            val action = HomeFragmentDirections.actionHomeFragmentToFestivalChooserFragment()
            findNavController().navigate(action)
        }

        binding.viewModel = viewModel

        //glideApp werkt met een cache dus logo zal niet elke keer moeten worden afgehaald van het internet
        viewModel.getLogo().observe( this, Observer { logoRef ->
            if (context != null) {
                GlideApp.with(context!!) // nullcheck gedaan, context kan niet zonder !! gebruikt worden hier
                    .load(logoRef)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(binding.logoView)
            }
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
            /*wanneer er naar festival selectie scherm gegaan wordt, moeten de listeners verwijderd worden van het festival
            zodat er geen meldingen,... meer binnenkomen. Als ze toch terugkeren naar hetzelfde festival moet de data niet
            volledig opnieuw geladen worden aangezien die al in firebase cache zit (foto's zitten in glide cache)
             */
            viewModel.removeId()
            val action = HomeFragmentDirections.actionHomeFragmentToFestivalChooserFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }

    //zorgen dat homescreen niet kan draaien
    override fun onResume() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        super.onResume()
    }

    override fun onPause() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR;
        super.onPause()
    }
}
