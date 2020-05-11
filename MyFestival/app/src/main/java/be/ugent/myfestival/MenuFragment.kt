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
import be.ugent.myfestival.models.FoodStand
import be.ugent.myfestival.utilities.GlideApp
import be.ugent.myfestival.utilities.InjectorUtils
import be.ugent.myfestival.viewmodels.FestivalViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

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
        val id: String = args.foodstandId

        val adapter = MenuAdapter()

        viewModel.getFoodstand(id).observe(viewLifecycleOwner, Observer {foodstand ->
            binding.foodstandNameMenuView.text = foodstand.name

            GlideApp.with(binding.foodstandImgMenuView.context)
                .load(foodstand.logoRef)
                .placeholder(R.drawable.no_internet)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(binding.foodstandImgMenuView)

            adapter.menuList = foodstand.menu
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