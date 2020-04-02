package com.example.myfestival

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfestival.adapters.FoodStandAdapter
import com.example.myfestival.databinding.FoodFragmentBinding
import com.example.myfestival.models.FoodStand
import com.example.myfestival.utilities.InjectorUtils
import com.example.myfestival.viewmodels.FestivalViewModel


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

        //TODO: met livedata werken
        //binding.viewModel = viewModel

        val data = viewModel.getFoodstandList()
        initAdapter(binding, data)

        return binding.root
    }

    fun initAdapter(binding: FoodFragmentBinding, data: List<FoodStand>) {
        binding.foodstandRecyclerView.apply {
            adapter = FoodStandAdapter(data) { foodStand: FoodStand -> handleItemClick(foodStand)}
            layoutManager = LinearLayoutManager(this.context)
            setHasFixedSize(true)
        }
    }

    fun handleItemClick(foodStand: FoodStand) {
        // Toast.makeText(this.context, "Clicked: ${foodStand.name}", Toast.LENGTH_LONG).show()
        val action = FoodFragmentDirections.actionFoodFragmentToMenuFragment(foodStand.id)
        findNavController().navigate(action)

    }




}
