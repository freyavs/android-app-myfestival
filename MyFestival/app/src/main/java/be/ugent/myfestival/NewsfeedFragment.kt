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

        var adapter = NewsfeedAdapter()

        viewModel.getNewsfeedItems().observe(viewLifecycleOwner, Observer { posts -> adapter.posts = posts})

        binding.stageRecycler.adapter = adapter
        binding.stageRecycler.layoutManager = LinearLayoutManager(this.context)
        binding.stageRecycler.setHasFixedSize(true)

        //todo(!! wegwerken ...)
        //val decorator = DividerItemDecoration(this.context, LinearLayoutManager.VERTICAL)
        //decorator.setDrawable(ContextCompat.getDrawable(this.context!!, R.drawable.spacer)!!)

        //binding.stageRecycler.addItemDecoration(decorator)

        return binding.root
    }

}
