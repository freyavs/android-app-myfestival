package be.ugent.myfestival

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import be.ugent.myfestival.MyFestival.Companion.CHANNEL_1_ID
import be.ugent.myfestival.databinding.HomeFragmentBinding
import be.ugent.myfestival.utilities.GlideApp
import be.ugent.myfestival.utilities.InjectorUtils
import be.ugent.myfestival.viewmodels.FestivalViewModel

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = HomeFragmentBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        context?: return binding.root

        val viewModel by activityViewModels<FestivalViewModel> {
            InjectorUtils.provideFestivalViewModelFactory()
        }
        viewModel.setId(context?.getSharedPreferences("FestivalPreference", Context.MODE_PRIVATE))

        //kijken of er een ID is, zo niet gaat het naar het festival kies scherm
        if(!viewModel.hasFestival()){
            val action = HomeFragmentDirections.actionHomeFragmentToFestivalChooserFragment()
            findNavController().navigate(action)
        }

        binding.viewModel = viewModel
        
        binding.newsfeedHandler = View.OnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToNewsfeedFragment()
            findNavController().navigate(action)
        }

        binding.foodHandler = View.OnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToFoodFragment()
            findNavController().navigate(action)
        }

        binding.lineupHandler = View.OnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToLineupFragment()
            findNavController().navigate(action)
        }

        binding.mapHandler = View.OnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToMapFragment()
            findNavController().navigate(action)
        }
        binding.festivalChooserHandler = View.OnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToFestivalChooserFragment()
            findNavController().navigate(action)
        }

    // notificaties
        binding.notificationHandler = View.OnClickListener {
            val builder = this.context?.let { context -> NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.newsfeed_50dp)
                .setContentTitle("Title of newsfeed notification")
                .setContentText("This is the message of the newsfeed notification")
                .setPriority(NotificationCompat.PRIORITY_HIGH)}

            with (this.context?.let { context -> NotificationManagerCompat.from(context) }) {
                if (builder != null) {
                    this?.notify(1, builder.build())
                }
            }
        }
        return binding.root
    }


}
