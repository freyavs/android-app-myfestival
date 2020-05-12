package be.ugent.myfestival

import android.content.pm.ActivityInfo
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

        viewModel.getStageCoord().observe(viewLifecycleOwner, Observer { hashmap ->
            for(stage in hashmap.keys){
                val coords = hashmap[stage]
                val lat : Double = coords!![0]
                val long : Double = coords[1]
                val concert = LatLng(lat,long)
                googleMap.addMarker(MarkerOptions().position(concert).title(stage).icon(getMarkerIcon("#FFB565")))
            }
        })
        viewModel.getFoodstandCoord().observe(viewLifecycleOwner, Observer { hashmap ->
            for(stage in hashmap.keys){
                val coords = hashmap[stage]
                val lat : Double = coords!![0]
                val long : Double = coords[1]
                val concert = LatLng(lat,long)
                googleMap.addMarker(MarkerOptions().position(concert).title(stage).icon(getMarkerIcon("#37966F")))
            }
        })
        var welcomeString = ""
        viewModel.getWelcomeString().observe(viewLifecycleOwner, Observer { name ->
            welcomeString = name
        })
        viewModel.getCoordsFestival().observe(viewLifecycleOwner, Observer{ coords ->
            val lat : Double = coords[0]
            val long : Double = coords[1]
            val entrance = LatLng(lat, long)
            googleMap.addMarker(MarkerOptions().position(entrance).title(welcomeString).icon(getMarkerIcon("#FF6F59")))
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(15.0f))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(entrance))
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
        return inflater.inflate(R.layout.map_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onResume() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        super.onResume()
    }

    override fun onPause() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR;
        super.onPause()
    }
}
