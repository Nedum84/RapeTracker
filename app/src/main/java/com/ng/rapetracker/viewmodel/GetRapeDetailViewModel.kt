package com.ng.rapetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ng.rapetracker.model.RapeDetail
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

    /**
     * init{} is called immediately when this ViewModel is created.
     */
    init {
        viewModelScope.launch {
            getRapeDetailRepo.getRapeDetails()
        }
    }

    val allRapeDetails = getRapeDetailRepo.rapeDetails
    val allRapeDetails2: LiveData<List<RapeDetail>> = database.rapeDetailDao.getAllRapeDetail()


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}