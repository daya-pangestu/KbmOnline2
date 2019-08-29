package com.appm.kbmonline.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapViewModel() : ViewModel() {
    val originLiveData  = MutableLiveData<Boolean>()
    val dstinationLiveData = MutableLiveData<Boolean>()

    val twoPointLiveData = MutableLiveData<Boolean>()


    fun setOrigin(pointExist : Boolean) {
      originLiveData.value = pointExist
    }

    fun setDestination(pointExist : Boolean){
        dstinationLiveData.value = pointExist
    }

    fun isSearchCompleted() : LiveData<Boolean>{
        return if (originLiveData.value == true && dstinationLiveData.value == true) {
            twoPointLiveData.value = true
            twoPointLiveData
        } else {
            twoPointLiveData.value = false
            twoPointLiveData
        }
    }
}