package com.ng.gbv_tracker.network

import com.ng.gbv_tracker.UrlHolder.URL_ROOT
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitConstant {

    companion object{
        val client =  OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .build()

        //    Retrofit with json response
        val RetrofitConstantGET: Retrofit = Retrofit.Builder()
            .baseUrl(URL_ROOT)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
