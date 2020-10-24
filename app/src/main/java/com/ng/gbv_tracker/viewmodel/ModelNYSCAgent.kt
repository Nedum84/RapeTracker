package com.ng.gbv_tracker.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.ng.gbv_tracker.Event
import com.ng.gbv_tracker.model.NYSCagent
import com.ng.gbv_tracker.repository.RepoNYSCAgent
import com.ng.gbv_tracker.room.DatabaseRoom
import com.ng.gbv_tracker.utils.ClassSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class ModelNYSCAgent(application: Application) : AndroidViewModel(application) {

    private val database = DatabaseRoom.getDatabaseInstance(application)
    private val viewModelJob = SupervisorJob()//OR Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val nyscRepo = RepoNYSCAgent(database)

    private val myDetails = ClassSharedPreferences(application).getCurUserDetail()

    val nyscAgents = database.nYSCagentDao.getAll()


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        viewModelScope.launch {
            nyscRepo.getAgents()
        }
    }

    val feedBack = nyscRepo.feedBack


    fun refreshCourse(){
        viewModelScope.launch {
            nyscRepo.getAgents()
        }
    }




    //Item category clicking
    private var _curItemCategory = MutableLiveData<Event<NYSCagent>>()
    val curItemCategory:LiveData<Event<NYSCagent>> get() = _curItemCategory
    fun setItemCategory(item:NYSCagent){
        _curItemCategory.value = Event(item)
    }










    /**
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ModelNYSCAgent::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ModelNYSCAgent(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}


