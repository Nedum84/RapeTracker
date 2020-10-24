package com.ng.gbv_tracker.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.ng.gbv_tracker.UrlHolder
import com.ng.gbv_tracker.model.RapeDetail
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
//    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(UrlHolder.URL_ROOT)
    .build()

//WITHOUT MOSHI
private val retrofit2 = Retrofit.Builder()
        .baseUrl(UrlHolder.URL_ROOT)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


interface GetRapeDetailService {
    /**
     * Returns a Coroutine [Deferred] [List] of [MarsProperty] which can be fetched with await() if
     * in a Coroutine scope.
     * The @GET annotation indicates that the "realestate" endpoint will be requested with the GET
     * HTTP method
     */
    @GET("get_rape_detail.php")
    fun getRapeDetail(@Query("type") type: String,
                      @Query("user_id_org_type") user_id_org_type: String,
                      @Query("last_id") last_inserted_id: String
    ):Deferred<List<RapeDetail>>
}
