package com.ng.rapetracker.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.ng.rapetracker.UrlHolder
import com.ng.rapetracker.model.*
import com.ng.rapetracker.network.*
import com.ng.rapetracker.room.DatabaseRoom
import com.ng.rapetracker.utils.ClassSharedPreferences
import com.ng.rapetracker.utils.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class GetRapeDetailRepo(private val database: DatabaseRoom, val application: Application) {


    var type:Int = 0
    var user_id_org_type = 0
    val prefs = ClassSharedPreferences(application)
    init {
        if (prefs.getAccessLevel() == 1){
            type = 1
            user_id_org_type = Gson().fromJson(prefs.getCurUserDetail(), User::class.java).id
        }else if (prefs.getAccessLevel() == 2){
            type = 2
            user_id_org_type = Gson().fromJson(prefs.getCurNYSCAgent(), NYSCagent::class.java).agent_id
        }else{
            type = 3
            user_id_org_type = Gson().fromJson(prefs.getCurOrgDetail(), Organization::class.java).orgType
        }
    }



//    val rapeDetails = database.rapeDetailDao.getAllRapeDetail()
    val rapeDetails: LiveData<List<RapeDetail>> = if (prefs.getAccessLevel()==1){
        database.rapeDetailDao.getUserRapeDetail(user_id_org_type.toLong())
    }else if (prefs.getAccessLevel()==2){
        database.rapeDetailDao.getNYSCAgentRapeDetail(user_id_org_type.toLong())
    }else if (prefs.getAccessLevel()==3){
        database.rapeDetailDao.getSupportRapeDetail(user_id_org_type.toLong())
    }else{
        database.rapeDetailDao.getAllRapeDetail()
    }

    val rapeSupportType: LiveData<List<RapeSupportType>> = database.rapeSupportTypeDao.getAllRapeSupport()
    private val lastInsertId = database.rapeDetailDao.getRecentRapeDetail().value?.id ?: "0"
//    private val lastInsertId = "00"

    suspend fun getRapeDetails(){
        val retrofit = Retrofit.Builder()
            .baseUrl(UrlHolder.URL_ROOT)
//            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

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
                e.printStackTrace()
//                _status.value = MarsApiStatus.ERROR
//                _properties.value = ArrayList()
            }
        }
    }
}