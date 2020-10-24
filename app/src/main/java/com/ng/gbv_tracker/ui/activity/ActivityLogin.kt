package com.ng.gbv_tracker.ui.activity

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ng.gbv_tracker.R
import com.ng.gbv_tracker.UrlHolder
import com.ng.gbv_tracker.model.*
import com.ng.gbv_tracker.network.GetDefaultListService
import com.ng.gbv_tracker.network.RetrofitConstant
import com.ng.gbv_tracker.network.ServerResponse
import com.ng.gbv_tracker.room.DatabaseRoom
import com.ng.gbv_tracker.utils.ClassAlertDialog
import com.ng.gbv_tracker.utils.ClassSharedPreferences
import com.ng.gbv_tracker.utils.toast
import com.ng.gbv_tracker.viewmodel.ModelLoginActivity
import com.ng.gbv_tracker.viewmodel.ModelSarcsSupport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActivityLogin : AppCompatActivity() {
    lateinit var viewModelLoginActivity: ModelLoginActivity
    lateinit var databaseRoom: DatabaseRoom
    lateinit var prefs:ClassSharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
//        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        databaseRoom = DatabaseRoom.getDatabaseInstance(this)
        prefs = ClassSharedPreferences(this)


        val viewModelFactory = ModelLoginActivity.Factory(application)
        viewModelLoginActivity = ViewModelProvider(this, viewModelFactory).get(ModelLoginActivity::class.java)
//        viewModelLoginActivity.setGotoMainActivity(false)

        if (prefs.isLoggedIn()){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        if(prefs.getOpeningForTheFirstTime()) {
//            getDefaultValues()
            val sarcsSupport = ViewModelProvider(this, ModelSarcsSupport.Factory(application)).get(ModelSarcsSupport::class.java)


            CoroutineScope(IO).launch {
                try {
                    sarcsSupport.addToDb()//add sarcs support

                    val obj = JSONObject(UrlHolder.defaultLists)
                    addListsToDb(obj)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }



    fun getDefaultValues(){

        val defaultListService = RetrofitConstant.RetrofitConstantGET.create(GetDefaultListService::class.java)

        defaultListService.getListRequest("all").enqueue(object: Callback<ServerResponse> {
            override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                t.printStackTrace()
                this@ActivityLogin.toast("No internet connect!")
            }
            override fun onResponse(call: Call<ServerResponse>, response: Response<ServerResponse>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {

                        val serverResponse = response.body()

                        try {
                            val obj = JSONObject(serverResponse!!.otherDetail!!)
                            if ((serverResponse.success as Boolean)){
                                if (serverResponse.respMessage == "ok") {

                                    CoroutineScope(IO).launch {
                                        addListsToDb(obj)
                                    }


                                } else {
                                    this@ActivityLogin.toast(serverResponse.respMessage!!)
                                }
                            }else{
                                this@ActivityLogin.toast(serverResponse.respMessage!!)
                            }

                        } catch (e: JSONException) {
                            e.printStackTrace()
                            this@ActivityLogin.toast("Response Error! Try again...$response")
                        }
                    }
                } else {
                    ClassAlertDialog(this@ActivityLogin).alertMessage("An error occurred, Try again...")
                }
            }

        })
    }

    suspend fun addListsToDb(jsonObject:JSONObject){
        val countrysz = jsonObject.getJSONArray("countrysz")
        val statesx = jsonObject.getJSONArray("statesx")
        val rape_typexs = jsonObject.getJSONArray("rape_typexs")
        val rape_support_typezs = jsonObject.getJSONArray("rape_support_typezs")
        val rape_type_of_victimxzs = jsonObject.getJSONArray("rape_type_of_victimxzs")

        val allCountry = mutableListOf<Country>()
        for (i in 0 until countrysz.length()) {
            val eachList = countrysz.getJSONObject(i)

            val eachRow = Country(
                eachList.getInt("id"),
                eachList.getString("iso"),
                eachList.getString("name"),
                eachList.getString("nicename"),
                eachList.getString("iso3"),
                eachList.getString("numcode"),
                eachList.getString("phonecode")
            )
            allCountry.add(eachRow)
        }
        databaseRoom.getCountryDao().upSertCountry(allCountry)

        //STATE
        val allStates = mutableListOf<State>()
        for (i in 0 until statesx.length()) {
            val eachList = statesx.getJSONObject(i)

            val eachRow = State(
                eachList.getInt("id"),
                eachList.getString("state"),
                eachList.getInt("country_id")
            )
            allStates.add(eachRow)
        }
        databaseRoom.getStateDao().upSertState(allStates)

        //RAPE TYPE
        val rapeTypes = mutableListOf<RapeType>()
        for (i in 0 until rape_typexs.length()) {
            val eachList = rape_typexs.getJSONObject(i)

            val eachRow = RapeType(
                eachList.getInt("id"),
                eachList.getString("rape_type"),
                eachList.getString("rape_description")
            )
            rapeTypes.add(eachRow)
        }
        databaseRoom.rapeTypeDao.insertAllRapeType(rapeTypes)

        //RAPE SUPPORT TYPE
        val rapeSupportTypes = mutableListOf<RapeSupportType>()
        for (i in 0 until rape_support_typezs.length()) {
            val eachList = rape_support_typezs.getJSONObject(i)

            val eachRow = RapeSupportType(
                eachList.getInt("id"),
                eachList.getString("rape_support_type")
            )
            rapeSupportTypes.add(eachRow)
        }
        databaseRoom.rapeSupportTypeDao.insertRapeSupport(rapeSupportTypes)

        //RAPE TYPE OF VICTIM
        val rapeTypeOfVictim = mutableListOf<RapeTypeOfVictim>()
        for (i in 0 until rape_type_of_victimxzs.length()) {
            val eachList = rape_type_of_victimxzs.getJSONObject(i)

            val eachRow = RapeTypeOfVictim(
                eachList.getInt("id"),
                eachList.getString("rape_type_of_victim")
            )
            rapeTypeOfVictim.add(eachRow)
        }
        databaseRoom.rapeTypeOfVictimDao.insertAllRapeTypeOfVictim(rapeTypeOfVictim)



        ClassSharedPreferences(this).setOpeningForTheFirstTime(false)
    }
}