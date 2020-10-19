package com.ng.rapetracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ng.rapetracker.Event
import com.ng.rapetracker.model.LatLong


class ModelAddressPick: ViewModel() {


    val userAddressPick: LiveData<Event<LatLong>> get() = _userAddressPick
    private val _userAddressPick = MutableLiveData<Event<LatLong>>()
    fun setUserAddressPick(data: LatLong) {
        _userAddressPick.value = Event(data)
    }


}