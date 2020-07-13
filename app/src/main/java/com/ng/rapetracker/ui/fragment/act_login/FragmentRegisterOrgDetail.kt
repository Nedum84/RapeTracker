package com.ng.rapetracker.ui.fragment.act_login

import android.app.Activity
import android.app.Application
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
import com.ng.rapetracker.R
import com.ng.rapetracker.databinding.FragmentRegisterOrgDetailBinding
import com.ng.rapetracker.model.RapeSupportType
import com.ng.rapetracker.network.LoginRegService
import com.ng.rapetracker.network.RetrofitConstant
import com.ng.rapetracker.network.ServerResponse
import com.ng.rapetracker.room.DatabaseRoom
import com.ng.rapetracker.ui.activity.MainActivity
import com.ng.rapetracker.ui.fragment.BaseFragment
import com.ng.rapetracker.ui.fragment.act_login.FragmentRegisterOrgDetailArgs.fromBundle
import com.ng.rapetracker.utils.ClassProgressDialog
import com.ng.rapetracker.utils.ClassSharedPreferences
import com.ng.rapetracker.utils.toast
import com.ng.rapetracker.viewmodel.ModelLoginActivity
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentRegisterOrgDetail : BaseFragment() {
    private lateinit var selectedStateId: String
    private lateinit var selectedCountryId: String
    lateinit var binding: FragmentRegisterOrgDetailBinding
    private lateinit var thisContext:Activity
    lateinit var appCtx: Application
    lateinit var viewModelLoginActivity:ModelLoginActivity

    lateinit var databaseRoom: DatabaseRoom
    lateinit var rapeSupportType: RapeSupportType

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Get a reference to the binding object and inflate the fragment views.
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_org_detail, container, false)
        thisContext = requireActivity()
        appCtx= requireNotNull(activity).application
        databaseRoom = DatabaseRoom.getDatabaseInstance(thisContext)

        val viewModelFactory = ModelLoginActivity.Factory(appCtx)
        viewModelLoginActivity = ViewModelProvider(this, viewModelFactory).get(
            ModelLoginActivity::class.java)

        rapeSupportType = FragmentRegisterOrgDetailArgs.fromBundle(requireArguments()).rapeSupportType


        binding.orgPassword.transformationMethod = PasswordTransformationMethod()
        binding.supportTypeTitle.text = rapeSupportType.rapeSupportType
        launch {
            countrySpinnerInitialize()
            stateSpinnerInitialize()
        }

        binding.orgRegBtn.setOnClickListener {

            launch {
                registerOrg()
            }
        }


        return binding.root
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
            context.let {it!!.toast("All the fields are required...")}
        }else if(orgPassword.length <6){
            requireContext().toast("Password should not be at least 6 characters")
        }else{
            val pDialog = ClassProgressDialog(thisContext, "Processing Registration...")
            pDialog.createDialog()

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
                    pDialog.dismissDialog()
                    t.printStackTrace()
                    requireContext().toast("No internet connect!")
                }

                override fun onResponse(call: Call<ServerResponse>,response: Response<ServerResponse>) {
                    pDialog.dismissDialog()
                    if (response.isSuccessful) {
                        if (response.body() != null) {

                            val serverResponse = response.body()

                            try {
                                val obj = JSONObject(serverResponse!!.otherDetail!!)
                                if ((serverResponse.success as Boolean)){
                                    if (serverResponse.respMessage == "ok") {
                                        context!!.toast("Registration successful...")


                                        //Save and Redirect...
                                        viewModelLoginActivity.saveUserDetails(obj, ClassSharedPreferences(thisContext))

                                        activity?.let {
                                            startActivity(Intent(it, MainActivity::class.java))
                                            it.finish()
                                        }

                                    } else {
                                        context!!.toast(serverResponse.respMessage!!)
                                    }
                                }else{
                                    context!!.toast(serverResponse.respMessage!!)
                                }

                            } catch (e: JSONException) {
                                e.printStackTrace()
                                context!!.toast("Response Error! Try again...")
                            }
                        }
                    } else {
                        context!!.toast("An error occurred, Try again")
                    }
                }

            })
        }
    }


    private suspend fun countrySpinnerInitialize(){
        val countryList = databaseRoom.getCountryDao().getAllCountry().sortedBy { it.id }
        val countryNameArray = arrayListOf<String>()
        val countryIdArray = arrayListOf<String>()

//        countryNameArray.add("Country")
//        countryIdArray.add("-1")
        countryNameArray.add(countryList[155].nicename.capitalize()+"(+${countryList[155].phonecode})")
        countryIdArray.add("${countryList[155].id}")

        for (element in countryList) {
            countryNameArray.add(element.nicename.capitalize()+"(+${element.phonecode})")
            countryIdArray.add("${element.id}")
        }
        binding.orgCountry?.setItem(countryNameArray)

        binding.orgCountry?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCountryId = countryIdArray[position]
            }

        }

    }

    private suspend fun stateSpinnerInitialize(){
        val stateList = databaseRoom.getStateDao().getAllState().sortedBy { it.id }
        val stateNameArray = arrayListOf<String>()
        val stateIdArray = arrayListOf<String>()

//        stateNameArray.add("Select State")
//        stateIdArray.add("-1")
        for (element in stateList) {
            stateNameArray.add(element.name)
            stateIdArray.add("${element.id}")

        }
        binding.orgState?.setItem(stateNameArray)
        binding.orgState?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedStateId = stateIdArray[position]
            }

        }

    }
}