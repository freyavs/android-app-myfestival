package be.ugent.myfestival

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import be.ugent.myfestival.utilities.InjectorUtils
import be.ugent.myfestival.viewmodels.FestivalChooserViewModel
import be.ugent.myfestival.viewmodels.FestivalViewModel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val viewModel by activityViewModels<FestivalViewModel>{
            InjectorUtils.provideFestivalViewModelFactory()
        }
        var welcomeString: String = ""
        viewModel.getWelcomeString().observe(viewLifecycleOwner, Observer { name ->
            welcomeString = name
        })
        viewModel.getCoordsFestival().observe(viewLifecycleOwner, Observer{ coords ->
            val lat : Double = coords.get(0)
            val long : Double = coords.get(1)

            val entrance = LatLng(lat, long)
            googleMap.addMarker(MarkerOptions().position(entrance).title(welcomeString))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(entrance))
        })
        viewModel.getStageCoord().observe(viewLifecycleOwner, Observer { hashmap ->
            for(stage in hashmap.keys){
                val coords = hashmap[stage]
                val lat : Double = coords!!.get(0)
                val long : Double = coords!!.get(1)
                val concert = LatLng(lat,long)
                googleMap.addMarker(MarkerOptions().position(concert).title(stage))
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(concert))
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}
