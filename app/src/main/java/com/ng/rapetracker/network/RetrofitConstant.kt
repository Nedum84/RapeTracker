package com.ng.rapetracker.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitConstant {

    companion object{

        //    Retrofit with json response
        val retrofitWithJsonRes = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
