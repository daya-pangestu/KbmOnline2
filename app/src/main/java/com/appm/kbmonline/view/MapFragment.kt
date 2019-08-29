package com.appm.kbmonline.view


import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.appm.kbmonline.R
import com.appm.kbmonline.viewmodel.MapViewModel
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import com.mapbox.geojson.FeatureCollection.fromFeature
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import kotlinx.android.synthetic.main.fragment_map.*
import timber.log.Timber
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource

class MapFragment : Fragment(), OnMapReadyCallback {

    lateinit var mapboxMap: MapboxMap
    lateinit var markerViewManager: MarkerViewManager
    companion object{
        const val REQUEST_CODE_ORIGIN = 200
        const val REQUEST_CODE_DESTIONATION =  232
    }

    lateinit var mapViewModel: MapViewModel
     var origin : LatLng? = null
    var destination : LatLng? = null
    var st : String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_map, container, false)
st = "gerw"

        mapViewModel = ViewModelProviders.of(this).get(MapViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fmapView.onCreate(savedInstanceState)
        fmapView.getMapAsync(this)



    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        this.markerViewManager = MarkerViewManager(fmapView,mapboxMap)
        mapboxMap.setStyle(Style.MAPBOX_STREETS)
        fMapSearchOrigin.setOnClickListener {
            searchPlace(REQUEST_CODE_ORIGIN)
        }

        fMapSearchDestination.setOnClickListener {
            searchPlace(REQUEST_CODE_DESTIONATION)
        }
    }

    private fun searchPlace(requestCode: Int){
        val intent = Mapbox.getAccessToken()?.let {
            PlaceAutocomplete.IntentBuilder()
                .accessToken(it)
                .placeOptions(PlaceOptions.builder()
                    .backgroundColor(Color.parseColor("#EEEEEE"))
                    .limit(10)
                    .hint(
                        if (requestCode == REQUEST_CODE_ORIGIN) "Origin"
                        else "Destination")
                    .build(PlaceOptions.MODE_CARDS))
                .build(this@MapFragment.activity as Activity?)
        }
        startActivityForResult(intent, requestCode)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_ORIGIN && resultCode == RESULT_OK && data != null){
            val feature = PlaceAutocomplete.getPlace(data)
            Timber.i("${feature.text()} and latitude longitude ${feature.center()}")

            origin = LatLng(feature.center()!!.latitude(),feature.center()!!.longitude())


            moveCameraPosition(feature, requestCode = REQUEST_CODE_ORIGIN)

        }else if (requestCode == REQUEST_CODE_DESTIONATION && resultCode == RESULT_OK && data != null){
            val feature = PlaceAutocomplete.getPlace(data)
            Timber.i("${feature.text()} and ${feature.center()}")

            destination = LatLng(feature.center()!!.latitude(),feature.center()!!.longitude())

            moveCameraPosition(feature,requestCode = REQUEST_CODE_DESTIONATION)
        }
    }

    private fun moveCameraPosition(feature : CarmenFeature, requestCode : Int = REQUEST_CODE_ORIGIN){
        mapboxMap.let {
            it.style?.let {style ->
                val  source :GeoJsonSource? = style.getSourceAs("geojsonlayerid")
                return@let source?.setGeoJson(fromFeature(feature.toJson() as com.mapbox.geojson.Feature))
            }
        }

        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
            CameraPosition.Builder()
                .target(
                    LatLng(
                        feature.center()!!.latitude(),
                        feature.center()!!.longitude())
                )
                .zoom(14.0)
                .build()),500)

        val imageview = ImageView(this@MapFragment.context)
        imageview.apply {
            setImageResource(if (requestCode == REQUEST_CODE_ORIGIN) R.drawable.ic_location_on_purple else R.drawable.ic_location_on_orange)
            layoutParams = FrameLayout.LayoutParams(56, 56)
        }

        val marker = MarkerView(LatLng(feature.center()!!.latitude(), feature.center()!!.longitude()), imageview)
        marker.let{
            markerViewManager.addMarker(it)
        }


        if (origin != null && destination != null ) {
            val latLngBounds = LatLngBounds.Builder()
                .include(origin!!)
                .include(destination!!)
                .build()

            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 50))
        } else {
            Timber.i("origin is $origin? and destination is $destination? ")
        }

    }



    override fun onStart() {
        super.onStart()
        fmapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        fmapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        fmapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        fmapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        fmapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        fmapView.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fmapView.onDestroy()
        markerViewManager.onDestroy()
    }


}
