package com.ng.gbv_tracker.ui.fragment.act_login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ng.gbv_tracker.R
import com.ng.gbv_tracker.databinding.FragmentRegisterVictimBinding
import com.ng.gbv_tracker.model.LatLong
import com.ng.gbv_tracker.network.LoginRegService
import com.ng.gbv_tracker.network.RetrofitConstant
import com.ng.gbv_tracker.network.ServerResponse
import com.ng.gbv_tracker.ui.activity.MainActivity
import com.ng.gbv_tracker.ui.fragment.BaseFragment
import com.ng.gbv_tracker.utils.ClassProgressDialog
import com.ng.gbv_tracker.utils.toast
import com.ng.gbv_tracker.viewmodel.ModelAddressPick
import com.ng.gbv_tracker.viewmodel.ModelLoginActivity
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
    lateinit var viewModelLoginActivity:ModelLoginActivity
    lateinit var regBtn:TextView

    var latLong = LatLong(0.0,0.0, "Lagos")


    private val modelAddressPick: ModelAddressPick by lazy { requireActivity().run {
        ViewModelProvider(this).get(ModelAddressPick::class.java)
    } }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Get a reference to the binding object and inflate the fragment views.
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_victim, container, false)


        binding.userPassword.transformationMethod = PasswordTransformationMethod()
        regBtn = binding.registerBtn
        genderSpinnerInitialize()

        binding.goToLogin.setOnClickListener {
            this.findNavController().navigate(FragmentRegisterVictimDirections.actionFragmentRegisterVictimToFragmentLogin())
        }


        binding.userAddress.setOnClickListener {
            val addrFrag = if (latLong.long!=0.0) FragmentPickAddress.newInstance(latLong) else FragmentPickAddress()

            requireActivity().run {
                addrFrag.show(this.supportFragmentManager, tag)
            }
        }


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModelFactory = ModelLoginActivity.Factory(application)
        viewModelLoginActivity = requireActivity().run{
            ViewModelProvider(this, viewModelFactory).get(ModelLoginActivity::class.java)
        }
        binding.registerBtn.setOnClickListener {
            registerUser()
        }


        modelAddressPick.userAddressPick.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let {
                latLong = it
                binding.userAddress.text = it.address
            }
        })
    }

    private fun registerUser(){
            val userName = binding.userName.text.trim().toString()
            val userMobileNo = binding.userMobileNo.text.trim().toString()
            val userEmail = binding.userEmail.text.trim().toString()
            val userAge = binding.userAge.text.trim().toString()
            val userCountry = selectedCountryId
            val userGender = selectedGenderId
            val userState = selectedStateId
//            val userAddress = binding.userAddress.text.trim().toString()
            val userPassword = binding.userPassword.text.trim().toString()

            if (isEmpty(userName)){
                context.let {it!!.toast("Enter your name")}
            }else if(isEmpty(userMobileNo)){
                requireContext().toast("Enter your phone no.")
            }else if(isEmpty(userEmail)){
                requireContext().toast("Enter your email address")
            }else if(userGender=="-1"){
                requireContext().toast("Select your gender")
            }else if(isEmpty(userAge)){
                requireContext().toast("Your age is required")
            }else if(isEmpty(latLong.address)){
                requireContext().toast("Enter your address")
            }else if(userPassword.length <6){
                requireContext().toast("Password should not be at least 6 characters")
            }else{
                val pDialog = ClassProgressDialog(thisContext, "Processing Registration...")
                pDialog.createDialog()

                val registerService = RetrofitConstant.RetrofitConstantGET.create(LoginRegService::class.java)
                registerService.registerRequest(
                    request_type = "victim_register",
                    user_name = userName,
                    user_mobile_no = userMobileNo,
                    user_email = userEmail,
                    user_gender = userGender,
                    user_age = userAge,
                    user_address = latLong.address,
                    latitude = "${latLong.lat}",
                    longitude = "${latLong.long}",
                    user_password = userPassword
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
                                            viewModelLoginActivity.saveUserDetails(obj)
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
}