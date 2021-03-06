package com.ng.gbv_tracker.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.ng.gbv_tracker.Event
import com.ng.gbv_tracker.model.RapeDetail
import com.ng.gbv_tracker.model.RapeType
import com.ng.gbv_tracker.model.RapeTypeOfVictim
import com.ng.gbv_tracker.room.DatabaseRoom

class RapeComplainFormViewModel(rapeDetail: RapeDetail, application: Application) : AndroidViewModel(application) {


    private val formRapeDetail = rapeDetail

    private val database = DatabaseRoom.getDatabaseInstance(application)
    val allRapeType: LiveData<List<RapeType>> = database.rapeTypeDao.getAllRapeType()
    val allRapeTypeOfVictim: LiveData<List<RapeTypeOfVictim>> = database.rapeTypeOfVictimDao.getAllRapeOfVictimType()
    val allRapeSupportType  = database.rapeSupportTypeDao.getAllRapeSupport()






    //next button in the form
    val rapeComplainLogSuccessful: LiveData<Event<Boolean>> get() = _rapeComplainLogSuccessful
    private val _rapeComplainLogSuccessful = MutableLiveData<Event<Boolean>>()
    fun setRapeComplainLogSuccessful(data: Boolean) {
        _rapeComplainLogSuccessful.value = Event(data)
    }

    //Prev button in the form
    val formBackBtn: LiveData<Event<Boolean>> get() = _formBackBtn
    private val _formBackBtn = MutableLiveData<Event<Boolean>>()
    fun backBtnClicked(data: Boolean) {
        _formBackBtn.value = Event(data)
    }

    //cancel button in the form
    val formCancelBtn: LiveData<Event<Boolean>> get() = _formCancelBtn
    private val _formCancelBtn = MutableLiveData<Event<Boolean>>()
    fun cancelBtnClicked(data: Boolean) {
        _formCancelBtn.value = Event(data)
    }












    /**
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(private val rDetail: RapeDetail, private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RapeComplainFormViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RapeComplainFormViewModel(rDetail, app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
