package be.ugent.myfestival

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import be.ugent.myfestival.adapters.StageAdapter
import be.ugent.myfestival.databinding.StageFragmentBinding
import be.ugent.myfestival.models.Stage

class StageFragment(val stage: Stage) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : StageFragmentBinding = StageFragmentBinding.inflate(inflater, container, false)
        context ?: return binding.root

        binding.stageName.text = stage.name
        binding.stageRecycler.adapter =
            StageAdapter(stage.concerts)
        binding.stageRecycler.layoutManager = LinearLayoutManager(this.context)
        binding.stageRecycler.setHasFixedSize(true)


        return binding.root
    }
}
