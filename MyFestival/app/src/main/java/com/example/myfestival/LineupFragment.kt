package com.example.myfestival

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfestival.adapters.DayAdapter
import com.example.myfestival.adapters.StageAdapter
import com.example.myfestival.databinding.LineupFragmentBinding


/**
 * A simple [Fragment] subclass.
 */
class LineupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : LineupFragmentBinding = LineupFragmentBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val concertsMain = listOf<Concert>(Concert("Json Derulo", "18:30", "20:00"), Concert("Zwangere Guy", "20:30", "22:00"))
        val concertsForest = listOf<Concert>(Concert("Frank Ocean", "18:30", "20:00"), Concert("George Bucks", "20:30", "22:00"))
        val concertsHerbakker = listOf<Concert>(Concert("Evil Ponys", "18:30", "20:00"), Concert("Eddy de Ketalaeare", "20:30", "22:00"))

        val Mainstage = Stage(concertsMain)
        val Forestage = Stage(concertsForest)
        val Herbakkerstage = Stage(concertsHerbakker)

        val stages = listOf<Stage>(Mainstage, Forestage, Herbakkerstage)

        binding.dayRecycler.adapter = DayAdapter(stages)
        binding.dayRecycler.layoutManager = GridLayoutManager(this.context, 1, LinearLayoutManager.HORIZONTAL, false)
        binding.dayRecycler.setHasFixedSize(true)

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.lineup_fragment, container, false)
        return binding.root
    }

    
}
