package be.ugent.myfestival

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import be.ugent.myfestival.databinding.HomeFragmentBinding
import be.ugent.myfestival.utilities.InjectorUtils
import be.ugent.myfestival.viewmodels.FestivalViewModel

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*
        het ID uit de preffence halen
        val preference = context?.getSharedPreferences("FestivalPreference", Context.MODE_PRIVATE)
        Log.v("MyActivity", preference?.getString("ID",""))*/
        val binding = HomeFragmentBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this

        val viewModel by activityViewModels<FestivalViewModel> {
            InjectorUtils.provideFestivalViewModelFactory()
        }

        binding.viewModel = viewModel
        
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
