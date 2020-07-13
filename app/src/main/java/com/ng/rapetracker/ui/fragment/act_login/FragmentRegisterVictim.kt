package com.ng.rapetracker.ui.fragment.act_login

import android.annotation.SuppressLint
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
import android.widget.Toast
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
import com.ng.rapetracker.utils.ClassProgressDialog
import com.ng.rapetracker.utils.ClassSharedPreferences
import com.ng.rapetracker.utils.toast
import com.ng.rapetracker.viewmodel.ModelLoginActivity
import com.ng.rapetracker.viewmodel.ModelMainActivity
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

//        val viewModelFactory = ModelLoginActivity.Factory(appCtx)
//        viewModelLoginActivity = ViewModelProvider(viewLifecycleOwner, viewModelFactory).get(
//            ModelLoginActivity::class.java)


        binding.userPassword.transformationMethod = PasswordTransformationMethod()
        regBtn = binding.registerBtn
        genderSpinnerInitialize()
        countrySpinnerInitialize()
        launch {
            stateSpinnerInitialize(156)
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
        viewModelLoginActivity = requireActivity().run{
            ViewModelProvider(this, viewModelFactory).get(ModelLoginActivity::class.java)
        }
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
                val pDialog = ClassProgressDialog(thisContext, "Processing Registration...")
                pDialog.createDialog()

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
                        pDialog.dismissDialog()
                        t.printStackTrace()
                        requireContext().toast("No internet connection!")
                    }

                    override fun onResponse(call: Call<ServerResponse>,response: Response<ServerResponse>) {
                        pDialog.dismissDialog()

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
                                            activity?.let {
                                                startActivity(Intent(it, MainActivity::class.java))
                                                it.finish()
                                            }

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
        val genderNameArray = arrayListOf("Male", "Female")
        val genderIdArray = arrayListOf( "1", "2")


        binding.userGender?.setItem(genderNameArray)

        binding.userGender?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedGenderId = genderIdArray[position]
            }

        }


    }

    @SuppressLint("DefaultLocale")
    private fun countrySpinnerInitialize(){
        launch {
            val countryList = databaseRoom.getCountryDao().getAllCountry().sortedBy { it.id }
            val countryNameArray = arrayListOf<String>()
            val countryIdArray = arrayListOf<String>()

//            countryNameArray.add("Country")
//            countryIdArray.add("-1")
            countryNameArray.add(countryList[155].nicename.capitalize()+"(+${countryList[155].phonecode})")
            countryIdArray.add("${countryList[155].id}")

            for (element in countryList) {
                countryNameArray.add(element.nicename.capitalize()+"(+${element.phonecode})")
                countryIdArray.add("${element.id}")
            }
            binding.userCountry?.setItem(countryNameArray)

            binding.userCountry?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selectedCountryId = countryIdArray[position]
                    if (selectedCountryId!=="-1"){
                        launch {
                            stateSpinnerInitialize(selectedCountryId.toInt())
                        }
                    }
                }

            }

        }

    }

    private suspend fun stateSpinnerInitialize(country_id:Int){
        val stateList = databaseRoom.getStateDao().getStateByCountryId(country_id).sortedBy { it!!.id }
        val stateNameArray = arrayListOf<String>()
        val stateIdArray = arrayListOf<String>()

//        stateNameArray.add("State")
//        stateIdArray.add("-1")
        for (element in stateList) {
            stateNameArray.add(element!!.name)
            stateIdArray.add("${element.id}")

        }
        binding.userState?.setItem(stateNameArray)

        binding.userState?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedStateId = stateIdArray[position]
            }

        }

    }
}