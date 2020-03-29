package com.example.myfestival

import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfestival.adapters.StageAdapter
import com.example.myfestival.data.Stage
import com.example.myfestival.databinding.StageFragmentBinding

class StageFragment(val stage: Stage) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : StageFragmentBinding = StageFragmentBinding.inflate(inflater, container, false)
        context ?: return binding.root

        binding.stageName.text = stage.name
        binding.stageRecycler.adapter = StageAdapter(stage.concerts)
        binding.stageRecycler.layoutManager = LinearLayoutManager(this.context)
        binding.stageRecycler.setHasFixedSize(true)

        //todo(!! wegwerken ...)
        val decorator = DividerItemDecoration(this.context, LinearLayoutManager.VERTICAL)
        decorator.setDrawable(ContextCompat.getDrawable(this.context!!, R.drawable.spacer)!!)

        binding.stageRecycler.addItemDecoration(decorator)

        return binding.root
    }
}
