package com.appm.kbmonline.view


import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.appm.kbmonline.R
import com.appm.kbmonline.viewmodel.MapViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.from
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.geojson.Point
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
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import kotlinx.android.synthetic.main.fragment_map.*
import timber.log.Timber
import kotlinx.android.synthetic.main.fmap_bottom_sheet.*
import org.jetbrains.anko.support.v4.find
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapFragment : Fragment(), OnMapReadyCallback {

    lateinit var mMapboxMap: MapboxMap
    lateinit var mMarkerViewManager: MarkerViewManager

    companion object {
        const val REQUEST_CODE_ORIGIN = 200
        const val REQUEST_CODE_DESTIONATION = 232
    }

    var mOrigin: LatLng? = null
    var mDestination: LatLng? = null
    var mMarkerOrigin: MarkerView? = null
    var mMrkerDestination: MarkerView? = null
    lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    lateinit var mMapViewModel: MapViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        mMapViewModel = ViewModelProviders.of(this).get(MapViewModel::class.java)
        val bottomsheet: ConstraintLayout = view.findViewById(R.id.bottomSheetStart)
        bottomSheetBehavior = from(bottomsheet)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fmapView.onCreate(savedInstanceState)
        fmapView.getMapAsync(this)

        if (::bottomSheetBehavior.isInitialized) bottomSheetBehavior.isHideable = false


        mMapViewModel.getCarmenFeatureOrigin().observe(viewLifecycleOwner, Observer { })
        mMapViewModel.getCarmenFeatureDestination().observe(viewLifecycleOwner, Observer { })

    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        mMapboxMap = mapboxMap
        mMarkerViewManager = MarkerViewManager(fmapView, mapboxMap)


        mapboxMap.setStyle(Style.MAPBOX_STREETS)
        fMapSearchOrigin.setOnClickListener {
            searchPlace(REQUEST_CODE_ORIGIN)
        }

        fMapSearchDestination.setOnClickListener {
            searchPlace(REQUEST_CODE_DESTIONATION)
        }
        fmapBtnStart.setOnClickListener {
            if (mOrigin != null && mDestination != null) {
                val latLngBounds = LatLngBounds.Builder()
                    .include(mOrigin!!)
                    .include(mDestination!!)
                    .build()

                mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 80))
                findRoute()


            } else {
                Timber.i("mOrigin is $mOrigin? and mDestination is $mDestination? ")
            }
        }
    }

    private fun searchPlace(requestCode: Int) {
        val intent = Mapbox.getAccessToken()?.let {
            PlaceAutocomplete.IntentBuilder()
                .accessToken(it)
                .placeOptions(
                    PlaceOptions.builder()
                        .backgroundColor(Color.parseColor("#EEEEEE"))
                        .limit(10)
                        .hint(
                            if (requestCode == REQUEST_CODE_ORIGIN) "Origin" else "Destination"
                        )
                        .build(PlaceOptions.MODE_CARDS)
                )
                .build(this@MapFragment.activity as Activity?)
        }
        startActivityForResult(intent, requestCode)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_ORIGIN && resultCode == RESULT_OK && data != null) {
            val feature = PlaceAutocomplete.getPlace(data)
            Timber.i("${feature.text()} and latitude longitude ${feature.center()}")

            if (mOrigin != null) {
                mMarkerViewManager.removeMarker(this.mMarkerOrigin!!)
            }
            mOrigin = LatLng(feature.center()!!.latitude(), feature.center()!!.longitude())
            fMapSearchOrigin.text = feature.text()

            moveCameraToOrigin(mOrigin!!)

            mMapViewModel.setCarmenFeatureOrigin(feature)


        } else if (requestCode == REQUEST_CODE_DESTIONATION && resultCode == RESULT_OK && data != null) {
            val feature = PlaceAutocomplete.getPlace(data)
            Timber.i("${feature.text()} and ${feature.center()}")
            if (mDestination != null) {
                mMarkerViewManager.removeMarker(this.mMrkerDestination!!)
            }
            mDestination = LatLng(feature.center()!!.latitude(), feature.center()!!.longitude())
            fMapSearchDestination.text = feature.text()
            moveCameraToDestination(mDestination!!)
            mMapViewModel.setCarmenFeatureDestination(feature)
        }
    }

    private fun moveCameraToOrigin(
        origin: LatLng,
        titile: String = "bandung",
        snippet: String = "bandung"
    ) {
        mMapboxMap.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder()
                    .target(origin)
                    .zoom(6.0)
                    .build()
            )
            , 500
        )

        val imageview = ImageView(this@MapFragment.context)
        imageview.apply {
            setImageResource(R.drawable.ic_location_on_purple)
            layoutParams = FrameLayout.LayoutParams(92, 92)
        }

        mMarkerOrigin = MarkerView(origin, imageview)
        mMarkerOrigin.let {
            mMarkerViewManager.addMarker(it!!)
        }
    }

    private fun moveCameraToDestination(
        destination: LatLng,
        titile: String = "jakarta",
        snippet: String = "jakarta"
    ) {
        mMapboxMap.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder()
                    .target(destination)
                    .zoom(6.0)
                    .build()
            ), 500
        )

        val imageview = ImageView(this@MapFragment.context)
        imageview.apply {
            setImageResource(R.drawable.ic_location_on_orange)
            layoutParams = FrameLayout.LayoutParams(92, 92)
        }

        mMrkerDestination = MarkerView(destination, imageview)
        mMrkerDestination.let {
            mMarkerViewManager.addMarker(it!!)
        }
    }


    fun findRoute() {
        val origin: Point
        val destination: Point
        if (mOrigin != null && mDestination != null) {
            origin = Point.fromLngLat(mOrigin!!.longitude, mOrigin!!.latitude)
            destination = Point.fromLngLat(mDestination!!.longitude, mDestination!!.latitude)


            NavigationRoute.builder(context)
                .accessToken(Mapbox.getAccessToken()!!)
                .origin(origin)
                .destination(destination)
                .alternatives(true)
                .build()
                .getRoute(object : Callback<DirectionsResponse> {
                    override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {}

                    override fun onResponse(
                        call: Call<DirectionsResponse>,
                        response: Response<DirectionsResponse>
                    ) {

                        if (response.body() == null) {
                            Log.e(
                                "TAG",
                                "No routes found, make sure you set the right user and access token."
                            )
                            return
                        } else if (response.body()!!.routes().size < 1) {
                            Log.e("TAG", "No routes found")
                            return
                        }

                        val currentRoute = response.body()!!.routes().get(0)
                        var navigationMapRoute: NavigationMapRoute? = null
                        // Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute()
                        } else {
                            navigationMapRoute =
                                NavigationMapRoute(
                                    null,
                                    fmapView,
                                    mMapboxMap,
                                    R.style.NavigationMapRoute
                                )
                        }
                        navigationMapRoute.addRoute(currentRoute)
                    }
                })
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
        mMarkerViewManager.onDestroy()
    }
}
