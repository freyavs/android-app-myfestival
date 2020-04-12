package be.ugent.myfestival

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import be.ugent.myfestival.adapters.DayAdapter
import be.ugent.myfestival.databinding.LineupFragmentBinding
import be.ugent.myfestival.utilities.InjectorUtils
import be.ugent.myfestival.viewmodels.LineupViewModel
import kotlinx.android.synthetic.main.lineup_fragment.*


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

        val button = Button(this.context)
        button.setText("A Button")
        buttonsLayout.addView(button)

        /*
        adapter =
            DayAdapter(this.childFragmentManager)
        binding.stageViewer.adapter = adapter

        viewModel.getCurrentStages().observe(viewLifecycleOwner, Observer { stages -> adapter.notifyChange(stages)} )

        binding.previousDayHandler = View.OnClickListener {
            viewModel.previousDayClicked()
        }
        binding.nextDayHandler = View.OnClickListener {
            viewModel.nextDayClicked()
        }
        */
        binding.setLifecycleOwner(this)


        return binding.root
    }
}
