package com.ng.gbv_tracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ng.gbv_tracker.Event
import com.ng.gbv_tracker.model.LatLong


class ModelAddressPick: ViewModel() {


    val userAddressPick: LiveData<Event<LatLong>> get() = _userAddressPick
    private val _userAddressPick = MutableLiveData<Event<LatLong>>()
    fun setUserAddressPick(data: LatLong) {
        _userAddressPick.value = Event(data)
    }


}