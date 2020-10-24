package com.ng.gbv_tracker.network

import com.google.gson.annotations.SerializedName
import com.ng.gbv_tracker.model.RapeDetail
import retrofit2.Call
import retrofit2.http.*


class ServerResponse {
    // variable name should be same as in the json response from php
    @SerializedName("success")
    val success:Boolean? = null
    @SerializedName("resp_message")
    val respMessage: String? = null
    @SerializedName("other_detail")
    val otherDetail: String? = null
}

data class ServerResponse2 (
    // variable name should be same as in the json response from php
    @SerializedName("success")
    val success:Boolean = false,
    @SerializedName("resp_message")
    val respMessage:String = ""
)


interface LoginRegService {
    @Multipart
    @POST("login.php")
    fun loginRequest(
        @Part("request_type") request_type:String,
        @Part("email_mobile_no") email_mobile_no:String,
        @Part("password") password:String
    ): Call<ServerResponse>


    @Multipart
    @POST("register_user.php")
    fun registerRequest(
        @Part("request_type") request_type:String,
        @Part("user_name") user_name:String,
        @Part("user_mobile_no") user_mobile_no:String,
        @Part("user_email") user_email:String,
        @Part("user_gender") user_gender:String,
        @Part("user_age") user_age:String,
        @Part("user_address") user_address:String,
        @Part("user_password") user_password:String,
        @Part("latitude") latitude:String,
        @Part("longitude") longitude:String
    ): Call<ServerResponse>

    @Multipart
    @POST("register_org.php")
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
    @Multipart
    @POST("add_rape_detail.php")
    fun rapeComplainRequest(
        @Part("request_type") request_type: String,
        @Part("rape_against_you") rape_against_you: String,
        @Part("type_of_victim") type_of_victim: String,
        @Part("type_of_rape") type_of_rape: String,
        @Part("rape_support_type") rape_support_type: String,
        @Part("rape_address") rape_address: String,
        @Part("rape_description") rape_description: String,
        @Part("user_id") user_id: String,
        @Part("user_name") user_name: String,
        @Part("user_age") user_age: String,
        @Part("date_added") date_added: String,
        @Part("nysc_agent_id") nysc_agent_id: String
    ): Call<ServerResponse>
}

interface GetDefaultListService {
    @GET("get_default_lists.php")
    fun getListRequest(@Query("filter") filter: String):
            Call<ServerResponse>
}



