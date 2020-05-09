package be.ugent.myfestival

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import be.ugent.myfestival.utilities.InjectorUtils
import be.ugent.myfestival.viewmodels.FestivalViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->

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
                if (coords != null) {
                    val lat: Double = coords.get(0)
                    val long: Double = coords.get(1)
                    val concert = LatLng(lat, long)
                    googleMap.addMarker(
                        MarkerOptions().position(concert).title(stage)
                            .icon(getMarkerIcon("#FFB565"))
                    )
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(concert))
                }
            }
        })
    }

    // method definition
    fun getMarkerIcon(color: String?): BitmapDescriptor? {
        val hsv = FloatArray(3)
        Color.colorToHSV(Color.parseColor(color), hsv)
        return BitmapDescriptorFactory.defaultMarker(hsv[0])
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
