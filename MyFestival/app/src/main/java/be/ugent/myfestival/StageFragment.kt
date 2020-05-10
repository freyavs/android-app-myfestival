package be.ugent.myfestival

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import be.ugent.myfestival.adapters.StageAdapter
import be.ugent.myfestival.databinding.StageFragmentBinding
import be.ugent.myfestival.models.Stage

class StageFragment(val stage: Stage, val isFirst: Boolean, val isLast: Boolean) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : StageFragmentBinding = StageFragmentBinding.inflate(inflater, container, false)
        context ?: return binding.root

        binding.stageName.text = stage.name

        //swipe help views
        binding.swipeLeft.visibility = if (isFirst) View.INVISIBLE else View.VISIBLE
        binding.swipeRight.visibility = if (isLast) View.INVISIBLE else View.VISIBLE


        binding.stageRecycler.adapter =
            StageAdapter(stage.concerts)
        binding.stageRecycler.layoutManager = LinearLayoutManager(this.context)
        binding.stageRecycler.setHasFixedSize(true)


        return binding.root
    }
    //zorgen dat stages in lineuppageviewer niet kunnen draaien
    override fun onResume() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        super.onResume()
    }

    override fun onPause() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR;
        super.onPause()
    }
}
