package com.example.myfestival

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfestival.adapters.FoodStandAdapter
import com.example.myfestival.adapters.MenuAdapter
import com.example.myfestival.databinding.MenuFragmentBinding
import com.example.myfestival.models.Dish
import com.example.myfestival.models.FoodStand
import com.example.myfestival.utilities.InjectorUtils
import com.example.myfestival.viewmodels.FestivalViewModel

class MenuFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: MenuFragmentBinding = MenuFragmentBinding.inflate(inflater, container, false)
        context?: return binding.root

        val viewModel by activityViewModels<FestivalViewModel> {
            InjectorUtils.provideFestivalViewModelFactory()
        }

        val args: MenuFragmentArgs by navArgs()
        binding.foodstandNameMenuView.text = args.foodstandName
        binding.foodstandImgMenuView.setImageResource(args.foodstandImg)

        val id: String = args.foodstandId
        val adapter = MenuAdapter()
        viewModel.getFoodstandMenu(id).observe(viewLifecycleOwner, Observer { menu -> adapter.menuList = menu })

        binding.menuRecyclerView.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(this.context)
            setHasFixedSize(true)
        }

        return binding.root
    }


}