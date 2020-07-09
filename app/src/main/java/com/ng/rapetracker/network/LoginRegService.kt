package com.ng.rapetracker.network

import com.google.gson.annotations.SerializedName
import com.ng.rapetracker.model.RapeDetail
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


class ServerResponse {
    // variable name should be same as in the json response from php
    @SerializedName("success")
    val success:Boolean? = null
    @SerializedName("resp_message")
    val respMessage: String? = null
}

data class ServerResponse2 (
    // variable name should be same as in the json response from php
    @SerializedName("success")
    val success:Boolean = false,
    @SerializedName("resp_message")
    val respMessage:String = ""
)


interface LoginRegService {
    @POST("login.php")
    fun loginRequest(
        @Part("request_type") request_type:String,
        @Part("email_mobile_no") email_mobile_no:String,
        @Part("password") password:String
    ): Call<ServerResponse>


    @POST("login.php")
    fun registerRequest(
        @Part("request_type") request_type:String,
        @Part("user_name") user_name:String,
        @Part("user_mobile_no") user_mobile_no:String,
        @Part("user_email") user_email:String,
        @Part("user_gender") user_gender:String,
        @Part("user_age") user_age:String,
        @Part("user_country") user_country:String,
        @Part("user_state") user_state:String,
        @Part("user_address") user_address:String,
        @Part("user_password") user_password:String
    ): Call<ServerResponse>

    @POST("login.php")
    fun registerOrgRequest(
        @Part("request_type") request_type:String,
        @Part("org_name") org_name:String,
        @Part("org_type") org_type:String,
        @Part("org_mobile_no") org_mobile_no:String,
        @Part("org_email") org_email:String,
        @Part("org_country") org_country:String,
        @Part("org_state") org_state:String,
        @Part("org_address") org_address:String,
        @Part("org_password") org_password:String
    ): Call<ServerResponse>


}

interface LogRapeComplainService {
    @POST("log_complain.php")
    fun rapeComplainRequest(
        @Part("request_type") request_type: String,
        @Part("rape_detail") rape_detail: String
    ): Call<ServerResponse>
}

