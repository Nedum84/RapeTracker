package com.happinesstonic.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.happinesstonic.Event


class ModelLoginActivity: ViewModel() {

    val gotoMainActivity: LiveData<Event<Boolean>> get() = _gotoMainActivity
    private val _gotoMainActivity = MutableLiveData<Event<Boolean>>()
    fun setGotoMainActivity(flag: Boolean) {
        _gotoMainActivity.value = Event(flag)
    }






}