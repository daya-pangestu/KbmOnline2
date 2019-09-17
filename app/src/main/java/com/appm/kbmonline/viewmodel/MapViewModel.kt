package com.appm.kbmonline.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapbox.api.geocoding.v5.models.CarmenFeature

class MapViewModel : ViewModel() {
    private val carmenFeatureOrigin = MutableLiveData<CarmenFeature>()
    private val carmenFeatureDestination = MutableLiveData<CarmenFeature>()


    fun setCarmenFeatureOrigin(feature: CarmenFeature){
        carmenFeatureOrigin.value = feature
    }

    fun getCarmenFeatureOrigin() = carmenFeatureOrigin


    fun setCarmenFeatureDestination(feature: CarmenFeature){
        carmenFeatureDestination.value = feature
    }

    fun getCarmenFeatureDestination() = carmenFeatureDestination


}