package be.ugent.myfestival

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import be.ugent.myfestival.adapters.MenuAdapter
import be.ugent.myfestival.databinding.MenuFragmentBinding
import be.ugent.myfestival.utilities.GlideApp
import be.ugent.myfestival.utilities.InjectorUtils
import be.ugent.myfestival.viewmodels.FestivalViewModel
import com.bumptech.glide.Glide

class MenuFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: MenuFragmentBinding = MenuFragmentBinding.inflate(inflater, container, false)
        context?: return binding.root

        val viewModel by activityViewModels<FestivalViewModel> {
            InjectorUtils.provideFestivalViewModelFactory()
        }

        val args: MenuFragmentArgs by navArgs()
        binding.foodstandNameMenuView.text = args.foodstandName

        //todo: met binding adapter
        GlideApp.with(binding.foodstandImgMenuView.context)
            .load(args.foodstandImg)
            .into(binding.foodstandImgMenuView);

        val id: String = args.foodstandId
        val adapter = MenuAdapter()
        viewModel.getFoodstandMenu(id).observe(viewLifecycleOwner, Observer {
            menu -> adapter.menuList = menu
            adapter.notifyDataSetChanged()
        })

        binding.menuRecyclerView.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(this.context)
            setHasFixedSize(true)
        }

        return binding.root
    }


}