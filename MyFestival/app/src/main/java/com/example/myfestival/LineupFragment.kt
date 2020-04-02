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

    var currentDay: Int = 0
    var day: String = ""
    //TODO ZEKER WEG DOEN
    lateinit var dagtext: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : LineupFragmentBinding = LineupFragmentBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val viewModel by activityViewModels<LineupViewModel> {
            InjectorUtils.provideLineupViewModelFactory()
        }

        adapter = DayAdapter(this.childFragmentManager)
        binding.stageViewer.adapter = adapter

        viewModel.getCurrentStages().observe(viewLifecycleOwner, Observer { stages -> adapter.notifyChange(stages)} )

        //todo: haal dag uit getCurrentDayString
        day = lineup[currentDay].day

        binding.day.text = day

        dagtext = binding.day


        binding.previousDayHandler = View.OnClickListener {
            /*if (currentDay > 0) {
                currentDay--
                adapter.notifyChange(lineup.days[currentDay].stages)
                day = lineup.days[currentDay].day
                dagtext.text = day
            }*/
            viewModel.nextDayClicked()
        }
        binding.nextDayHandler = View.OnClickListener {
            /*
            if (currentDay < lineup.days.size-1){
                currentDay++
                adapter.notifyChange(lineup.days[currentDay].stages)
                day = lineup.days[currentDay].day
                dagtext.text = day
            }
             */
            viewModel.previousDayClicked()
        }

        return binding.root
    }
}
