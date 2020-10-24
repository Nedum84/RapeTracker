package com.ng.gbv_tracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ng.gbv_tracker.Event


class ModelMainActivity: ViewModel() {



    val gotoFormDialog: LiveData<Event<String>> get() = _gotoFormDialog
    private val _gotoFormDialog = MutableLiveData<Event<String>>()
    fun setGotoFormFrag(msg: String) {
        _gotoFormDialog.value = Event(msg)
    }




}