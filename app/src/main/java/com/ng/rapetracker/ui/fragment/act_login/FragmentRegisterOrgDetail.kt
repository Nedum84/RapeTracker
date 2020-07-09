package com.ng.rapetracker.ui.fragment.act_login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.happinesstonic.viewmodel.ModelLoginActivity
import com.ng.rapetracker.R
import com.ng.rapetracker.databinding.FragmentRegisterOrgDetailBinding
import com.ng.rapetracker.model.RapeSupportType
import com.ng.rapetracker.network.LoginRegService
import com.ng.rapetracker.network.RetrofitConstant
import com.ng.rapetracker.network.ServerResponse
import com.ng.rapetracker.room.DatabaseRoom
import com.ng.rapetracker.ui.fragment.BaseFragment
import com.ng.rapetracker.ui.fragment.act_login.FragmentRegisterOrgDetailArgs.fromBundle
import com.ng.rapetracker.utils.toast
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentRegisterOrgDetail : BaseFragment() {
    private lateinit var selectedStateId: String
    private lateinit var selectedCountryId: String
    lateinit var binding: FragmentRegisterOrgDetailBinding
    private lateinit var thisContext:Activity

    lateinit var databaseRoom: DatabaseRoom
    lateinit var rapeSupportType: RapeSupportType

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Get a reference to the binding object and inflate the fragment views.
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_org_detail, container, false)
        thisContext = requireActivity()
        databaseRoom = DatabaseRoom.getDatabaseInstance(thisContext)

        rapeSupportType = FragmentRegisterOrgDetailArgs.fromBundle(requireArguments()).rapeSupportType




        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        binding.orgPassword.transformationMethod = PasswordTransformationMethod()
        launch {
            countrySpinnerInitialize()
            stateSpinnerInitialize()
        }

        binding.orgRegBtn.setOnClickListener {

            launch {
                registerOrg()
            }
        }
    }

    private suspend fun registerOrg(){
        val orgName = binding.orgName.text.trim().toString()
        val orgType = rapeSupportType.id.toString()
        val orgMobileNo = binding.orgMobileNo.text.trim().toString()
        val orgEmail = binding.orgEmail.text.trim().toString()
        val orgCountry = selectedCountryId
        val orgState = selectedStateId
        val orgAddress = binding.orgAddress.text.trim().toString()
        val orgPassword = binding.orgPassword.text.trim().toString()

        if (isEmpty(orgName) ||isEmpty(orgMobileNo) ||isEmpty(orgEmail) || (orgCountry=="-1")|| (orgState=="-1") ||isEmpty(orgAddress) ||isEmpty(orgPassword)){
            context.let {it!!.toast("Enter all required")}
        }else if(orgPassword.length <6){
            requireContext().toast("Password should not be at least 6 characters")
        }else{

            val registerOrgService = RetrofitConstant.retrofitWithJsonRes.create(LoginRegService::class.java)
            registerOrgService.registerOrgRequest(
                "org_register",
                orgName,
                orgType,
                orgMobileNo,
                orgEmail,
                orgCountry,
                orgState,
                orgAddress,
                orgPassword
            ).enqueue(object: Callback<ServerResponse> {
                override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                    requireContext().toast("No internet connect!")
                }

                override fun onResponse(call: Call<ServerResponse>,response: Response<ServerResponse>) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {

                            val serverResponse = response.body()


                            if (!(serverResponse!!.success as Boolean)){
                                context!!.toast(serverResponse.respMessage!!)
                            }else{
                                //Redirect...
                                val viewModelLoginActivity = ViewModelProvider(this@FragmentRegisterOrgDetail).get(
                                    ModelLoginActivity::class.java)
                                viewModelLoginActivity.setGotoMainActivity(true)
                            }
                        }
                    } else {
                        context!!.toast("An error occurred, Try again")
                    }
                }

            })
        }
    }


    private fun countrySpinnerInitialize(){
        val countryList = databaseRoom.getCountryDao().getAllCountry()
        val countryNameArray = arrayListOf<String>()
        val countryIdArray = arrayListOf<String>()

        countryNameArray.add("Country")
        countryIdArray.add("-1")
        for (element in countryList) {
            countryNameArray.add(element.name)
            countryIdArray.add("${element.id}")

        }
        val spinnerArrayAdapter = ArrayAdapter<String>(thisContext, android.R.layout.simple_spinner_dropdown_item, countryNameArray)
        //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.orgCountry?.adapter = spinnerArrayAdapter

        binding.orgCountry?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCountryId = countryIdArray[position]
            }

        }

    }

    private fun stateSpinnerInitialize(){
        val stateList = databaseRoom.getStateDao().getAllState()
        val stateNameArray = arrayListOf<String>()
        val stateIdArray = arrayListOf<String>()

        stateNameArray.add("Country")
        stateIdArray.add("-1")
        for (element in stateList) {
            stateNameArray.add(element.name)
            stateIdArray.add("${element.id}")

        }
        val spinnerArrayAdapter = ArrayAdapter<String>(thisContext, android.R.layout.simple_spinner_dropdown_item, stateNameArray)
        //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.orgState?.adapter = spinnerArrayAdapter

        binding.orgState?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedStateId = stateIdArray[position]
            }

        }

    }
}