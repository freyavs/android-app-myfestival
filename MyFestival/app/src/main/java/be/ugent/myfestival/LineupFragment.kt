package be.ugent.myfestival

import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import be.ugent.myfestival.adapters.DayAdapter
import be.ugent.myfestival.databinding.LineupFragmentBinding
import be.ugent.myfestival.utilities.InjectorUtils
import be.ugent.myfestival.viewmodels.FestivalViewModel
import be.ugent.myfestival.viewmodels.LineupViewModel
import kotlinx.android.synthetic.main.lineup_fragment.view.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter


/**
 * A simple [Fragment] subclass.
 */
class LineupFragment : Fragment() {

    lateinit var adapter: DayAdapter
    lateinit var viewModel : FestivalViewModel


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


        //todo: dit kan sws beter (er moet ook elke keer als er opnieuw naar lineup gekeken wordt op "vandaag" gestart worden, hoe?)
        viewModel.getAllDaysSorted().observe(viewLifecycleOwner, Observer { days ->
            val startDay : LocalDate
            if (days.contains(viewModel.getToday())){
                Log.d("myFestivalTag", "today is in list of days.." )
                startDay = viewModel.getToday()
            }
            else {
                startDay = days[0]
            }
            for (day in days){
                val button = RadioButton(this.context)
                button.setBackgroundResource(R.drawable.radio_background)
                button.buttonDrawable = StateListDrawable()
                button.textSize = 25F
                button.text = (day.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                val layout : LinearLayout = binding.root.toggle_group
                button.setOnClickListener { viewModel.clickedDay(day) }
                layout.addView(button)
                if (day === startDay){
                    Log.d("myFestivalTag", "select day" )
                    button.performClick()
                }
            }
        })

        adapter =
            DayAdapter(this.childFragmentManager)
        binding.stageViewer.adapter = adapter

        viewModel.getCurrentStages().observe(viewLifecycleOwner, Observer { stages -> adapter.notifyChange(stages)} )

        binding.setLifecycleOwner(this)


        return binding.root
    }

}
