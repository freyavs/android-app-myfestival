package com.example.myfestival

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfestival.adapters.NewsfeedAdapter
import com.example.myfestival.models.NewsfeedItem
import com.example.myfestival.databinding.NewsfeedFragmentBinding
import com.example.myfestival.utilities.InjectorUtils
import com.example.myfestival.viewmodels.FestivalViewModel

/**
 * A simple [Fragment] subclass.
 */
class NewsfeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : NewsfeedFragmentBinding = NewsfeedFragmentBinding.inflate(inflater, container, false)
        context ?: return binding.root

        //val logo = ResourcesCompat.getDrawable(resources, R.mipmap.bakfietslogo, null)
        //val img = ResourcesCompat.getDrawable(resources, R.mipmap.uberdope, null)

        val viewModel by activityViewModels<FestivalViewModel> {
            InjectorUtils.provideFestivalViewModelFactory()
        }

        //TODO: werken met livedata!
        //binding.viewModel = viewModel

        binding.stageRecycler.adapter = NewsfeedAdapter(viewModel.getNewsfeedItems())
        binding.stageRecycler.layoutManager = LinearLayoutManager(this.context)
        binding.stageRecycler.setHasFixedSize(true)

        //todo(!! wegwerken ...)
        //val decorator = DividerItemDecoration(this.context, LinearLayoutManager.VERTICAL)
        //decorator.setDrawable(ContextCompat.getDrawable(this.context!!, R.drawable.spacer)!!)

        //binding.stageRecycler.addItemDecoration(decorator)

        return binding.root
    }

}
