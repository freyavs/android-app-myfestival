package be.ugent.myfestival

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import be.ugent.myfestival.adapters.FoodStandAdapter
import be.ugent.myfestival.databinding.FoodFragmentBinding
import be.ugent.myfestival.models.FoodStand
import be.ugent.myfestival.utilities.InjectorUtils
import be.ugent.myfestival.viewmodels.FestivalViewModel
import be.ugent.myfestival.FoodFragmentDirections
import androidx.lifecycle.observe



/**
 * A simple [Fragment] subclass.
 */
class FoodFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FoodFragmentBinding = FoodFragmentBinding.inflate(inflater, container, false)
        context?: return binding.root

        val viewModel by activityViewModels<FestivalViewModel> {
            InjectorUtils.provideFestivalViewModelFactory()
        }

        val adapter = FoodStandAdapter { foodStand: FoodStand ->
                                               handleItemClick(foodStand) }

        viewModel.getFoodstandList().observe(viewLifecycleOwner, Observer {
            list -> adapter.foodstands = list
            adapter.notifyDataSetChanged()
        })

        binding.foodstandRecyclerView.adapter = adapter
        binding.foodstandRecyclerView.layoutManager = LinearLayoutManager(this.context)

        return binding.root
    }

    fun handleItemClick(foodStand: FoodStand) {
        val action = FoodFragmentDirections.actionFoodFragmentToMenuFragment(foodStand.id, foodStand.logo, foodStand.name)
        findNavController().navigate(action)

    }





}
