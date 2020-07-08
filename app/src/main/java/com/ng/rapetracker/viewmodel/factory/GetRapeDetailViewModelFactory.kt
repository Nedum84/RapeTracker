package com.ng.rapetracker.viewmodel.factory

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ng.rapetracker.model.RapeDetail
import com.ng.rapetracker.repository.GetRapeDetailRepo
import com.ng.rapetracker.room.DatabaseRoom
import com.ng.rapetracker.viewmodel.GetRapeDetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class GetRapeDetailViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GetRapeDetailViewModel::class.java)) {
            return GetRapeDetailViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
