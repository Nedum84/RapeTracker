package com.ng.rapetracker.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.ng.rapetracker.UrlHolder
import com.ng.rapetracker.model.LatLong
import com.ng.rapetracker.model.NYSCagent
import com.ng.rapetracker.network.RetrofitConstant
import com.ng.rapetracker.network.moshi
import com.ng.rapetracker.room.DatabaseRoom
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class RepoNYSCAgent(private val database: DatabaseRoom) {


    val nyscAgents : LiveData<List<NYSCagent>> = database.nYSCagentDao.getAll()

    val feedBack:LiveData<String> get() = _feedBack
    private val _feedBack  = MutableLiveData<String>().apply {
        value = "success"
    }

    suspend fun getAgents(latLong: LatLong = LatLong(0.0,0.0)){
        val retrofit = Retrofit.Builder()
            .baseUrl(UrlHolder.URL_ROOT)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        val service = retrofit
            .create(NYSCAgentService::class.java)
            .getAsync("${latLong.lat}", "${latLong.long}" )

        withContext(Dispatchers.IO) {
            try {
                val listResult = service.await()
                database.nYSCagentDao.upSert(listResult)
            } catch (e: Exception) {
                e.printStackTrace()
                _feedBack.postValue("network_error")
            }
        }
    }
}

interface NYSCAgentService {

    @GET("get_nysc_agent.php")
    fun getAsync(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String
    ): Deferred<List<NYSCagent>>
}
