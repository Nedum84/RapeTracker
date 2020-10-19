package com.ng.rapetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ng.rapetracker.model.RapeSupportType
import com.ng.rapetracker.repository.GetRapeDetailRepo
import com.ng.rapetracker.room.DatabaseRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class GetRapeDetailViewModel(application: Application): AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()//OR Job()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = DatabaseRoom.getDatabaseInstance(application)
    private val getRapeDetailRepo = GetRapeDetailRepo(database, application)



    init {
        viewModelScope.launch {
            getRapeDetailRepo.getRapeDetails()
        }
    }
    fun refreshRapeDetail(){
        viewModelScope.launch {
            getRapeDetailRepo.getRapeDetails()
        }
    }

    val allRapeDetails = getRapeDetailRepo.rapeDetails //same as = database.rapeDetailDao.getAllRapeDetail()
    val rapeSupportType: LiveData<List<RapeSupportType>> = getRapeDetailRepo.rapeSupportType


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}