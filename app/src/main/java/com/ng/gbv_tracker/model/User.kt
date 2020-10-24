package com.ng.gbv_tracker.model

class User (
    val id:Int,
    val userName:String,
    val userMobileNo:String,
    val userEmail:String,
    val userAge:Int,
    val userCountry:Int = 1,
    val userGender:Int,
    val userState:Int = 1,
    val latitude: String = "0.0",
    val longitude: String= "0.0",
    val userAddress:String,
    val userRegDate:String,
    val accessLevel:Int
)