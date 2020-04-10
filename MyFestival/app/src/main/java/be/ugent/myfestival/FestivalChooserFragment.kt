package be.ugent.myfestival

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import be.ugent.myfestival.adapters.FestivalChooserAdapter
import be.ugent.myfestival.databinding.FestivalChooserFragmentBinding
import be.ugent.myfestival.models.FestivalChooser
import be.ugent.myfestival.utilities.InjectorUtils
import be.ugent.myfestival.viewmodels.FestivalChooserViewModel


class FestivalChooserFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FestivalChooserFragmentBinding = FestivalChooserFragmentBinding.inflate(inflater, container, false)
        context?: return binding.root

        val viewModel by activityViewModels<FestivalChooserViewModel>{
            InjectorUtils.provideFestivalChooserViewModelFactory()
        }
        val adapter =
            FestivalChooserAdapter() { festivalChooser: FestivalChooser ->
                handleItemClick(festivalChooser)
            }
        viewModel.getFestivals().observe(viewLifecycleOwner, Observer { festivals -> adapter.festivalList = festivals })

        binding.festivalRecyclerView.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(this.context)
            setHasFixedSize(true)
        }

        return binding.root
    }

    private fun handleItemClick(festivalChooser: FestivalChooser) {

        val preference = context?.getSharedPreferences("FestivalPreference", Context.MODE_PRIVATE)
        val editor = preference?.edit()
        editor?.putString("ID",festivalChooser.id)
        editor?.apply()
        val action = FestivalChooserFragmentDirections.actionFestivalChooserFragmentToHomeFragment2()
        findNavController().navigate(action)
    }
}
