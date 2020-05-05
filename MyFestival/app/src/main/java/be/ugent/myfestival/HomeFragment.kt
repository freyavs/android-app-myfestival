package be.ugent.myfestival

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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


        viewModel.setId(context?.getSharedPreferences("FestivalPreference", Context.MODE_PRIVATE), context)

        //kijken of er een ID is, zo niet gaat het naar het festival kies scherm
        if(!viewModel.hasFestival()){
            val action = HomeFragmentDirections.actionHomeFragmentToFestivalChooserFragment()
            findNavController().navigate(action)
        }

        binding.viewModel = viewModel

        viewModel.getLogo().observe( this, Observer { logo ->
            GlideApp.with(context!!)
                //.load(viewModel.getLogOffline(context!!))
                .load(logo)
                //.diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(binding.logoView)
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
            val action = HomeFragmentDirections.actionHomeFragmentToMapFragment()
            findNavController().navigate(action)
        }
        binding.festivalChooserHandler = View.OnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToFestivalChooserFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }
}
