package be.ugent.myfestival

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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
                handleItemClick(festivalChooser, viewModel)
            }

        viewModel.getFestivals().observe(viewLifecycleOwner, Observer { festivals ->
            binding.loadingText.visibility = View.GONE //als festival binnenkomt mag loading tekst weg
            adapter.festivalList = festivals
            adapter.notifyDataSetChanged()
        })
        //when the text change, the festivals had to change
        binding.searchFestival.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.changeSearchValue(s.toString())
            }
        })
        binding.festivalRecyclerView.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(this.context)
            setHasFixedSize(true)
        }

        val preference = context?.getSharedPreferences("FestivalPreference", Context.MODE_PRIVATE)
        val editor = preference?.edit()
        editor?.putString("ID","")
        editor?.apply()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun handleItemClick(
        festivalChooser: FestivalChooser,
        viewModel: FestivalChooserViewModel
    ) {
        viewModel.changeSearchValue("")
        val preference = context?.getSharedPreferences("FestivalPreference", Context.MODE_PRIVATE)
        val editor = preference?.edit()
        editor?.putString("ID",festivalChooser.id)
        editor?.apply()
        val action = FestivalChooserFragmentDirections.actionFestivalChooserFragmentToHomeFragment()
        findNavController().navigate(action)
    }
}
