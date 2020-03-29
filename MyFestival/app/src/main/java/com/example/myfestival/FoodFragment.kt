package com.example.myfestival

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import be.ugent.utilities.InjectorUtils
import com.example.myfestival.adapters.FoodStandAdapter
import com.example.myfestival.data.FoodStandDataSet
import com.example.myfestival.databinding.FoodFragmentBinding
import com.example.myfestival.models.FoodStand
import com.example.myfestival.viewmodels.FestivalViewModel
import kotlinx.android.synthetic.main.food_fragment.*
import java.security.AccessController.getContext

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
        val data = FoodStandDataSet.createDataSet()
        initAdapter(data)

        return binding.root
    }

    fun initAdapter(data: List<FoodStand>) {
        foodstand_recycler_view.apply {
            adapter = FoodStandAdapter(data)
            layoutManager = LinearLayoutManager(this.context)
            setHasFixedSize(true)
        }
    }






}
