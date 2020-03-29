package com.example.myfestival

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myfestival.adapters.DayAdapter
import com.example.myfestival.data.Concert
import com.example.myfestival.data.LineupDataObject
import com.example.myfestival.data.Stage
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



        val adapter = DayAdapter(makeStageFragments(), this.childFragmentManager)

        binding.stageViewer.adapter = adapter

        return binding.root
    }

    fun makeStageFragments(): List<StageFragment>{

        var stageFragments = mutableListOf<StageFragment>()
        for (stage in LineupDataObject().getData().stages){
            stageFragments.add(StageFragment(stage))
        }
        return stageFragments
    }
}
