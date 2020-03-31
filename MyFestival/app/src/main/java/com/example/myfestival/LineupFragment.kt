package com.example.myfestival

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.example.myfestival.adapters.DayAdapter
import com.example.myfestival.databinding.LineupFragmentBinding
import com.example.myfestival.utilities.InjectorUtils
import com.example.myfestival.viewmodels.FestivalViewModel


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

        val viewModel by activityViewModels<FestivalViewModel> {
            InjectorUtils.provideFestivalViewModelFactory()
        }

        //TODO dit moet nog met livedata uit viewmodel
        //binding.viewModel = viewModel
        val lineup = viewModel.getLineup()

        adapter = DayAdapter(lineup.days[currentDay].stages, this.childFragmentManager)
        binding.stageViewer.adapter = adapter


        day = lineup.days[currentDay].day

        binding.day.text = day

        dagtext = binding.day


        binding.previousDayHandler = View.OnClickListener {
            if (currentDay > 0) {
                currentDay--
                adapter.notifyChange(lineup.days[currentDay].stages)
                day = lineup.days[currentDay].day
                dagtext.text = day
            }
        }
        binding.nextDayHandler = View.OnClickListener {
            if (currentDay < lineup.days.size-1){
                currentDay++
                adapter.notifyChange(lineup.days[currentDay].stages)
                day = lineup.days[currentDay].day
                dagtext.text = day
            }
        }

        return binding.root
    }
}
