package com.ng.gbv_tracker.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.google.gson.Gson
import com.ng.gbv_tracker.Event
import com.ng.gbv_tracker.model.*
import com.ng.gbv_tracker.room.DatabaseRoom
import com.ng.gbv_tracker.utils.ClassSharedPreferences
import org.json.JSONObject


class ModelLoginActivity(application: Application) : AndroidViewModel(application) {
    val appCtx = application
    val prefs:ClassSharedPreferences by lazy { ClassSharedPreferences(application) }
    init {

    }
    private val database = DatabaseRoom.getDatabaseInstance(application)
    val allRapeType: LiveData<List<RapeType>> = database.rapeTypeDao.getAllRapeType()
    val allRapeTypeOfVictim: LiveData<List<RapeTypeOfVictim>> = database.rapeTypeOfVictimDao.getAllRapeOfVictimType()
    val allRapeSupportType  = database.rapeSupportTypeDao.getAllRapeSupport()


    val gotoMainActivity: LiveData<Event<Boolean>> get() = _gotoMainActivity
    private val _gotoMainActivity = MutableLiveData<Event<Boolean>>()
    fun setGotoMainActivity(flag: Boolean) {
        _gotoMainActivity.value = Event(flag)
    }







    //saving user's details
    fun saveUserDetails(other_detail: JSONObject?) {
        try {
            val details = other_detail!!.getJSONArray("userDetails").getJSONObject(0)

            val accessLevel = details!!.getInt("access_level")
            if (accessLevel == 1){
                val user = User(
                    id = details.getInt("id"),
                    userName = details.getString("user_name"),
                    userMobileNo = details.getString("user_mobile_no"),
                    userEmail = details.getString("user_email"),
                    userGender = details.getInt("user_gender"),
                    userAge = details.getInt("user_age"),
                    userCountry = details.getInt("user_country"),
                    userState = details.getInt("user_state"),
                    userAddress = details.getString("user_address"),
                    latitude = details.getString("latitude"),
                    longitude =details.getString("longitude"),
                    userRegDate = details.getString("user_reg_date"),
                    accessLevel = accessLevel
                )
                prefs.setCurUserDetail(Gson().toJson(user))
            }else if (accessLevel == 2){//NYSC
                val nyscA = NYSCagent(
                    agent_id = details.getInt("agent_id"),
                    name = details.getString("name"),
                    state_code = details.getString("state_code"),
                    address = details.getString("address"),
                    latitude = details.getString("latitude"),
                    longitude = details.getString("longitude"),
                    mobile_no = details.getString("mobile_no"),
                    email = details.getString("email"),
                    cases_attended = details.getInt("cases_attended")
                )
                prefs.setCurNYSCAgent(Gson().toJson(nyscA))
            }else{//Org
                val org = Organization(
                    details.getInt("id"),
                    details.getString("org_name"),
                    details.getInt("org_type"),
                    details.getString("org_mobile_no"),
                    details.getString("org_email"),
                    details.getInt("org_country"),
                    details.getInt("org_state"),
                    details.getString("org_address"),
                    details.getString("org_reg_date"),
                    details.getInt("access_level")
                )
                prefs.setCurOrgDetail(Gson().toJson(org))
            }

            prefs.setAccessLevel(accessLevel)
        } catch (e: Exception) {
            e.printStackTrace()
        }



//        setGotoMainActivity(true)
    }











    /**
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ModelLoginActivity::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ModelLoginActivity(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}