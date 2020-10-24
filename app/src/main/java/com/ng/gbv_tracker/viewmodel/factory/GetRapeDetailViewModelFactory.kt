package com.ng.gbv_tracker.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ng.gbv_tracker.model.RapeDetail
import com.ng.gbv_tracker.repository.GetRapeDetailRepo
import com.ng.gbv_tracker.viewmodel.GetRapeDetailViewModel

class GetRapeDetailViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GetRapeDetailViewModel::class.java)) {
            return GetRapeDetailViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
