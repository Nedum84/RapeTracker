package com.ng.rapetracker.repository

import androidx.lifecycle.LiveData
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.ng.rapetracker.model.RapeDetail
import com.ng.rapetracker.network.*
import com.ng.rapetracker.room.DatabaseRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GetRapeDetailRepo(private val database: DatabaseRoom) {


//    val videos: LiveData<List<RapeDetail>> =
//        Transformations.map(database.rapeDetailDao.getAllRapeDetail()) {
//            it.asDomainModel()
//        }
    val rapeDetails: LiveData<List<RapeDetail>> = database.rapeDetailDao.getAllRapeDetail()
    private val lastInsertId = database.rapeDetailDao.getRecentRapeDetail()

    suspend fun getRapeDetails(){
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
//            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        val rapeDetailService = retrofit.create(GetRapeDetailService::class.java).getRapeDetail("12", "${lastInsertId!!.id}")

        withContext(Dispatchers.IO) {
            try {
                val listResult = rapeDetailService.await()
//                _status.value = MarsApiStatus.DONE
//                _properties.value = listResult
                database.rapeDetailDao.insertRapeDetail(listResult)
            } catch (e: Exception) {
//                _status.value = MarsApiStatus.ERROR
//                _properties.value = ArrayList()
            }
        }
    }
}