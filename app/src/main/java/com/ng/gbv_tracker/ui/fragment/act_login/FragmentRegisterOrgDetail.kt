package com.ng.gbv_tracker.ui.fragment.act_login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ng.gbv_tracker.R
import com.ng.gbv_tracker.databinding.FragmentRegisterOrgDetailBinding
import com.ng.gbv_tracker.model.RapeSupportType
import com.ng.gbv_tracker.network.LoginRegService
import com.ng.gbv_tracker.network.RetrofitConstant
import com.ng.gbv_tracker.network.ServerResponse
import com.ng.gbv_tracker.room.DatabaseRoom
import com.ng.gbv_tracker.ui.activity.MainActivity
import com.ng.gbv_tracker.ui.fragment.BaseFragment
import com.ng.gbv_tracker.utils.ClassProgressDialog
import com.ng.gbv_tracker.utils.toast
import com.ng.gbv_tracker.viewmodel.ModelLoginActivity
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentRegisterOrgDetail : BaseFragment() {
    private var selectedStateId: String = "-1"
    private var selectedCountryId: String = "-1"
    lateinit var binding: FragmentRegisterOrgDetailBinding
    lateinit var viewModelLoginActivity:ModelLoginActivity

    lateinit var databaseRoom: DatabaseRoom
    lateinit var rapeSupportType: RapeSupportType

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Get a reference to the binding object and inflate the fragment views.
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_org_detail, container, false)
        databaseRoom = DatabaseRoom.getDatabaseInstance(thisContext)

        val viewModelFactory = ModelLoginActivity.Factory(application)
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

            val registerOrgService = RetrofitConstant.RetrofitConstantGET.create(LoginRegService::class.java)
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
                                        viewModelLoginActivity.saveUserDetails(obj)

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