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


        viewModel.getAllDaysSorted().observe(viewLifecycleOwner, Observer { days ->
            val startDay : LocalDate
            val map = mapOf<String, String>("MONDAY" to "Maandag", "TUESDAY" to "Dinsdag", "WEDNESDAY" to "Woensdag",
            "THURSDAY" to "Donderdag", "FRIDAY" to "Vrijdag", "SATURDAY" to "Zaterdag", "SUNDAY" to "Zondag")

            //zorgt dat er op vandaag gestart wordt tenzij vandaag niet tussen de lineup days zit
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
                button.text = map.get(day.dayOfWeek.toString())
                val layout : LinearLayout = binding.root.toggle_group
                button.setOnClickListener { viewModel.clickedDay(day) }
                layout.addView(button)
                if (day === startDay){
                    Log.d("myFestivalTag", "select day" )
                    button.performClick()
                }
            }
        })

        adapter = DayAdapter(this.childFragmentManager)
        binding.stageViewer.adapter = adapter

        viewModel.getCurrentStages().observe(viewLifecycleOwner, Observer { stages -> adapter.notifyChange(stages)} )

        binding.setLifecycleOwner(this)

        return binding.root
    }

}
