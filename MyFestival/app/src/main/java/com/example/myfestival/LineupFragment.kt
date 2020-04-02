package com.example.myfestival

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.myfestival.adapters.DayAdapter
import com.example.myfestival.adapters.NewsfeedAdapter
import com.example.myfestival.databinding.LineupFragmentBinding
import com.example.myfestival.utilities.InjectorUtils
import com.example.myfestival.viewmodels.FestivalViewModel
import com.example.myfestival.viewmodels.LineupViewModel


/**
 * A simple [Fragment] subclass.
 */
class LineupFragment : Fragment() {

    lateinit var adapter: DayAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : LineupFragmentBinding = LineupFragmentBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val viewModel by activityViewModels<LineupViewModel> {
            InjectorUtils.provideLineupViewModelFactory()
        }

        binding.viewModel = viewModel

        adapter = DayAdapter(this.childFragmentManager)
        binding.stageViewer.adapter = adapter

        viewModel.getCurrentStages().observe(viewLifecycleOwner, Observer { stages -> adapter.notifyChange(stages)} )

        binding.previousDayHandler = View.OnClickListener {
            viewModel.nextDayClicked()
        }
        binding.nextDayHandler = View.OnClickListener {
            viewModel.previousDayClicked()
        }

        return binding.root
    }
}
