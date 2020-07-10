package com.ng.rapetracker.ui.fragment.act_login

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ng.rapetracker.R
import com.ng.rapetracker.databinding.FragmentRegisterVictimBinding
import com.ng.rapetracker.network.LoginRegService
import com.ng.rapetracker.network.RetrofitConstant
import com.ng.rapetracker.network.ServerResponse
import com.ng.rapetracker.room.DatabaseRoom
import com.ng.rapetracker.ui.activity.MainActivity
import com.ng.rapetracker.ui.fragment.BaseFragment
import com.ng.rapetracker.utils.ClassSharedPreferences
import com.ng.rapetracker.utils.toast
import com.ng.rapetracker.viewmodel.ModelLoginActivity
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentRegisterVictim : BaseFragment() {
    private var selectedStateId: String = "-1"
    private var selectedCountryId: String = "-1"
    private var selectedGenderId: String = "-1"
    lateinit var binding: FragmentRegisterVictimBinding
    private lateinit var thisContext:Activity
    lateinit var appCtx:Application
    lateinit var viewModelLoginActivity:ModelLoginActivity
    lateinit var regBtn:Button

    lateinit var databaseRoom: DatabaseRoom

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Get a reference to the binding object and inflate the fragment views.
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_victim, container, false)
        thisContext = requireActivity()
        appCtx= requireNotNull(activity).application
        databaseRoom = DatabaseRoom.getDatabaseInstance(thisContext)

        val viewModelFactory = ModelLoginActivity.Factory(appCtx)
        viewModelLoginActivity = ViewModelProvider(this, viewModelFactory).get(
            ModelLoginActivity::class.java)


        binding.userPassword.transformationMethod = PasswordTransformationMethod()
        regBtn = binding.registerBtn
        genderSpinnerInitialize()
        countrySpinnerInitialize()
        launch {
            stateSpinnerInitialize()
        }

        binding.goToLogin.setOnClickListener {
            this.findNavController().navigate(FragmentRegisterVictimDirections.actionFragmentRegisterVictimToFragmentLogin())
        }
        binding.regAsRapeSupport.setOnClickListener {
            this.findNavController().navigate(FragmentRegisterVictimDirections.actionFragmentRegisterVictimToFragmentRegisterOrgSupportType())
        }




        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModelFactory = ModelLoginActivity.Factory(appCtx)
        viewModelLoginActivity = ViewModelProvider(this, viewModelFactory).get(
            ModelLoginActivity::class.java)
        binding.registerBtn.setOnClickListener {

            registerUser()
        }
    }

    private fun registerUser(){

            val userName = binding.userName.text.trim().toString()
            val userMobileNo = binding.userMobileNo.text.trim().toString()
            val userEmail = binding.userEmail.text.trim().toString()
            val userAge = binding.userAge.text.trim().toString()
            val userCountry = selectedCountryId
//        val userGender = binding.userGender.text.trim().toString()
            val userGender = selectedGenderId
            val userState = selectedStateId
            val userAddress = binding.userAddress.text.trim().toString()
            val userPassword = binding.userPassword.text.trim().toString()

            if (isEmpty(userName) ||isEmpty(userMobileNo) ||isEmpty(userEmail) ||isEmpty(userAge) || (userGender=="-1")|| (userCountry=="-1") || (userState == "-1") ||isEmpty(userAddress) ||isEmpty(userPassword)){
                context.let {it!!.toast("All the fields are required")}
            }else if(userPassword.length <6){
                requireContext().toast("Password should not be at least 6 characters")
            }else{

                val registerService = RetrofitConstant.retrofitWithJsonRes.create(LoginRegService::class.java)
                registerService.registerRequest(
                    "victim_register",
                    userName,
                    userMobileNo,
                    userEmail,
                    userGender,
                    userAge,
                    userCountry,
                    userState,
                    userAddress,
                    userPassword
                ).enqueue(object: Callback<ServerResponse> {
                    override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                        t.printStackTrace()
                        requireContext().toast("No internet connection!")
                    }

                    override fun onResponse(call: Call<ServerResponse>,response: Response<ServerResponse>) {
                        if (response.isSuccessful) {
                            if (response.body() != null) {

                                val serverResponse = response.body()

                                if ((serverResponse!!.success as Boolean)){
                                    if (serverResponse.respMessage == "ok") {
                                        context!!.toast("Registration successful...")

                                        try{
                                            val obj = JSONObject(serverResponse!!.otherDetail!!)
                                            //Save and Redirect...
                                            viewModelLoginActivity.saveUserDetails(obj, ClassSharedPreferences(thisContext))
                                            startActivity(Intent(activity!!, MainActivity::class.java))
                                            activity!!.finish()

                                        } catch (e: JSONException) {
                                            e.printStackTrace()
                                            context!!.toast("Response Error! Try again...")
                                        }
                                    } else {
                                        context!!.toast(serverResponse.respMessage!!)
                                    }
                                }else{
                                    context!!.toast(serverResponse.respMessage!!)
                                }

                            }
                        } else {
                            context!!.toast("An error occurred, Try again")
                        }
                    }

                })
            }
//        }
    }


    private fun genderSpinnerInitialize(){
        val genderNameArray = arrayListOf("Gender", "Male", "Female")
        val genderIdArray = arrayListOf("-1", "1", "2")


        val spinnerArrayAdapter = ArrayAdapter(thisContext, android.R.layout.simple_spinner_dropdown_item, genderNameArray)
        //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.userGender?.adapter = spinnerArrayAdapter

        binding.userGender?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedGenderId = genderIdArray[position]
            }

        }

    }

    private fun countrySpinnerInitialize(){
        launch {
            val countryList = databaseRoom.getCountryDao().getAllCountry().sortedBy { it.id }
            val countryNameArray = arrayListOf<String>()
            val countryIdArray = arrayListOf<String>()

            countryNameArray.add("Country")
            countryIdArray.add("-1")
            countryNameArray.add(countryList[155].name)
            countryIdArray.add("${countryList[155].id}")

            for (element in countryList) {
                countryNameArray.add(element.name)
                countryIdArray.add("${element.id}")

            }
            val spinnerArrayAdapter = ArrayAdapter<String>(thisContext, android.R.layout.simple_spinner_dropdown_item, countryNameArray)
            //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.userCountry?.adapter = spinnerArrayAdapter

            binding.userCountry?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selectedCountryId = countryIdArray[position]
                }

            }

        }

    }

    private suspend fun stateSpinnerInitialize(){
        val stateList = databaseRoom.getStateDao().getAllState().sortedBy { it.id }
        val stateNameArray = arrayListOf<String>()
        val stateIdArray = arrayListOf<String>()

        stateNameArray.add("State")
        stateIdArray.add("-1")
        for (element in stateList) {
            stateNameArray.add(element.name)
            stateIdArray.add("${element.id}")

        }
        val spinnerArrayAdapter = ArrayAdapter<String>(thisContext, android.R.layout.simple_spinner_dropdown_item, stateNameArray)
        //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.userState?.adapter = spinnerArrayAdapter

        binding.userState?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedStateId = stateIdArray[position]
            }

        }

    }
}