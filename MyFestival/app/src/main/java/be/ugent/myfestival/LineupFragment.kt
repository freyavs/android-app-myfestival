package be.ugent.myfestival

import android.content.pm.ActivityInfo
import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.constraintlayout.widget.ConstraintSet
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
            val map = mapOf("MONDAY" to "Maandag", "TUESDAY" to "Dinsdag", "WEDNESDAY" to "Woensdag",
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
                button.text = map[day.dayOfWeek.toString()]
                button.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f)
                button.gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL

                val layout : LinearLayout = binding.root.toggle_group
                button.setOnClickListener { viewModel.clickedDay(day) }
                layout.addView(button)
                //zet de dag goed in viewmodel
                if (day === startDay){
                    button.performClick()
                }
            }
        })

        adapter = DayAdapter(this.childFragmentManager)
        binding.stageViewer.adapter = adapter

        viewModel.getCurrentStages().observe(viewLifecycleOwner, Observer { stages -> adapter.notifyChange(stages)} )

        binding.lifecycleOwner = this

        return binding.root
    }

    //zorgen dat lineup niet kan draaien
    override fun onResume() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        super.onResume()
    }

    override fun onPause() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR;
        super.onPause()
    }
}
