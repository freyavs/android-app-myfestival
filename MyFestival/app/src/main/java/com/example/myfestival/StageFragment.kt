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
import com.example.myfestival.databinding.StageFragmentBinding

class StageFragment(val stage: Stage) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : StageFragmentBinding = StageFragmentBinding.inflate(inflater, container, false)
        context ?: return binding.root

        binding.stageRecycler.adapter = StageAdapter(stage.concerts)
        binding.stageRecycler.layoutManager = LinearLayoutManager(this.context)
        binding.stageRecycler.setHasFixedSize(true)


        return binding.root
    }
}
