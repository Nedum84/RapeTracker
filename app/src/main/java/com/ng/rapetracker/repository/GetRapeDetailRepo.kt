package com.ng.rapetracker.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.ng.rapetracker.model.Organization
import com.ng.rapetracker.model.RapeDetail
import com.ng.rapetracker.model.RapeSupportType
import com.ng.rapetracker.model.User
import com.ng.rapetracker.network.*
import com.ng.rapetracker.room.DatabaseRoom
import com.ng.rapetracker.utils.ClassSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GetRapeDetailRepo(private val database: DatabaseRoom, val application: Application) {


//    val videos: LiveData<List<RapeDetail>> =
//        Transformations.map(database.rapeDetailDao.getAllRapeDetail()) {
//            it.asDomainModel()
//        }
    val rapeDetails: LiveData<List<RapeDetail>> = database.rapeDetailDao.getAllRapeDetail()
    val rapeSupportType: LiveData<List<RapeSupportType>> = database.rapeSupportTypeDao.getAllRapeSupport()
//    private val lastInsertId = database.rapeDetailDao.getRecentRapeDetail()
    private val lastInsertId = "00"

    suspend fun getRapeDetails(){
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
//            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        var type:Int = 0
        var user_id_org_type = 0
        val prefs = ClassSharedPreferences(application)
        if (prefs.getAccessLevel() == 1){
            type = 1
            user_id_org_type = Gson().fromJson(prefs.getCurUserDetail(), User::class.java).id
        }else if (prefs.getAccessLevel() == 2){
            type = 2
            user_id_org_type = Gson().fromJson(prefs.getCurOrgDetail(), Organization::class.java).orgType
        }
        val rapeDetailService = retrofit
            .create(GetRapeDetailService::class.java)
            .getRapeDetail("$type", "$user_id_org_type","${lastInsertId}")

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