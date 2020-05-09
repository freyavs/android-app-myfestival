package be.ugent.myfestival

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import be.ugent.myfestival.adapters.NewsfeedAdapter
import be.ugent.myfestival.utilities.InjectorUtils
import be.ugent.myfestival.viewmodels.FestivalViewModel
import androidx.lifecycle.Observer
import be.ugent.myfestival.databinding.NewsfeedFragmentBinding

class NewsfeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : NewsfeedFragmentBinding = NewsfeedFragmentBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val viewModel by activityViewModels<FestivalViewModel> {
            InjectorUtils.provideFestivalViewModelFactory()
        }

        val adapter = NewsfeedAdapter()

        viewModel.getNewsfeedItems().observe(viewLifecycleOwner, Observer {
            posts -> adapter.posts = posts
            adapter.notifyDataSetChanged()
        })

        binding.stageRecycler.adapter = adapter
        binding.stageRecycler.layoutManager = LinearLayoutManager(this.context)
        binding.stageRecycler.setHasFixedSize(true)

        return binding.root
    }

}
