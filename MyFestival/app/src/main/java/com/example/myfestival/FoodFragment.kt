package com.example.myfestival

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfestival.adapters.FoodStandAdapter
import com.example.myfestival.databinding.FoodFragmentBinding
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
        val adapter = FoodStandAdapter()
        viewModel.getFoodstandList().observe(viewLifecycleOwner, Observer { foodstands -> adapter.foodStandList = foodstands })

        binding.foodstandRecyclerView.apply {
          
            // master: this.adapter = adapter
          
           //menu: was met (data)
            adapter = FoodStandAdapter() { foodStand: FoodStand -> handleItemClick(foodStand)}

            layoutManager = LinearLayoutManager(this.context)
            setHasFixedSize(true)
        }

        return binding.root
    }

    fun handleItemClick(foodStand: FoodStand) {
        val action = FoodFragmentDirections.actionFoodFragmentToMenuFragment(foodStand.id, foodStand.foodstandImg, foodStand.name)
        findNavController().navigate(action)

    }





}
