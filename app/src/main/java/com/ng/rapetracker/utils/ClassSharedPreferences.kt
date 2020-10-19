package com.ng.rapetracker.utils

import android.content.Context
import com.google.gson.Gson
import com.ng.rapetracker.model.User

class  ClassSharedPreferences(val context: Context?){

    private val PREFERENCE_NAME = "rape_tracker_preference"
    private val PREFERENCE_USER_DETAIL = "cur_user_detail"
    private val PREFERENCE_ORG_DETAIL = "cur_org_detail"
    private val PREFERENCE_NYSC_AGENT_DETAIL = "cur_nysc_agent"
    private val PREFERENCE_ACCESS_LEVEL= "access_level"
    private val PREFERENCE_OPENING_FOR_THE_FIRST_TIME= "opening_for_the_first_time"


    private val preference = context?.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)!!


    //set Current User Detail
    fun setCurUserDetail(data:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_USER_DETAIL,data)
        editor.apply()
    }
    fun getCurUserDetail():String{
        return  preference.getString(PREFERENCE_USER_DETAIL,"")!!
    }

    //set Current Org Detail
    fun setCurOrgDetail(data:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_ORG_DETAIL,data)
        editor.apply()
    }
    fun getCurOrgDetail():String{
        return  preference.getString(PREFERENCE_ORG_DETAIL,"")!!
    }
    //set Current nysc Detail
    fun setCurNYSCAgent(data:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_NYSC_AGENT_DETAIL,data)
        editor.apply()
    }
    fun getCurNYSCAgent():String{
        return  preference.getString(PREFERENCE_NYSC_AGENT_DETAIL,"")!!
    }

    //set access level
    fun setAccessLevel(data:Int){
        val editor = preference.edit()
        editor.putInt(PREFERENCE_ACCESS_LEVEL,data)
        editor.apply()
    }
    fun getAccessLevel() = preference.getInt(PREFERENCE_ACCESS_LEVEL,0)


    //set first time app opening
    fun setOpeningForTheFirstTime(id:Boolean){
        val editor = preference.edit()
        editor.putBoolean(PREFERENCE_OPENING_FOR_THE_FIRST_TIME,id)
        editor.apply()
    }
    //get first time app opening
    fun getOpeningForTheFirstTime():Boolean{
        return  preference.getBoolean(PREFERENCE_OPENING_FOR_THE_FIRST_TIME,true)
    }

    fun isLoggedIn():Boolean{
        return if (getAccessLevel()==1)
            getCurUserDetail()!=""
        else if (getAccessLevel()==2)
            getCurNYSCAgent()!=""
        else
            getCurOrgDetail()!=""
    }
    fun logoutUser() : Boolean{
        val userEditor = preference.edit()
        userEditor.clear()
        userEditor.apply()


        return true
    }
}