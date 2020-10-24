package com.ng.gbv_tracker.ui.fragment.act_login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ng.gbv_tracker.databinding.FragmentRegisterNyscAgentBinding
import com.ng.gbv_tracker.model.LatLong
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
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

class FragmentRegisterNYSCAgent : BaseFragment() {
    lateinit var binding: FragmentRegisterNyscAgentBinding
    lateinit var viewModelLoginActivity:ModelLoginActivity

    val modelAddressPick: ModelAddressPick by lazy { requireActivity().run {
        ViewModelProvider(this).get(ModelAddressPick::class.java)
    } }


//    var latLong = LatLong(0.0,0.0)
    var latLong = LatLong(0.0,0.0, "Lagos")


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModelFactory = ModelLoginActivity.Factory(application)
        viewModelLoginActivity = ViewModelProvider(this, viewModelFactory).get(
            ModelLoginActivity::class.java)



        binding.agentPassword.transformationMethod = PasswordTransformationMethod()

        modelAddressPick.userAddressPick.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let {
                latLong = it
                binding.agentAddress.text = it.address
            }
        })

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentRegisterNyscAgentBinding.inflate(inflater)

        binding.agentRegBtn.setOnClickListener {

            registerNYSCAgent()
        }

        binding.agentAddress.setOnClickListener {
            val addrFrag = if (latLong.long!=0.0) FragmentPickAddress.newInstance(latLong) else FragmentPickAddress()

            requireActivity().run {
                addrFrag.show(this.supportFragmentManager, tag)
            }
        }


        binding.goToLogin.setOnClickListener {
            this.findNavController().navigate(FragmentRegisterNYSCAgentDirections.actionFragmentRegisterNYSCAgentToFragmentLogin())
        }
        return binding.root
    }


    private fun registerNYSCAgent(){
        val agentName = binding.agentName.text.trim().toString()
        val agentMobileNo = binding.agentMobileNo.text.trim().toString()
        val agentEmail = binding.agentEmail.text.trim().toString()
        val agentStateCode = binding.agentStateCode.text.trim().toString()
        val agentAddress = binding.agentAddress.text.trim().toString()
        val agentPassword = binding.agentPassword.text.trim().toString()

        if (isEmpty(agentName)){
            context.let {it!!.toast("All the fields are required...")}
        }else if(isEmpty(agentStateCode)){
            requireContext().toast("State code can't be blank")
        }else if(isEmpty(agentMobileNo)){
            requireContext().toast("Enter your phone no.")
        }else if(isEmpty(agentEmail)){
            requireContext().toast("Enter your email address")
        }else if(isEmpty(latLong.address)){
            requireContext().toast("Your address is required")
        }else if(agentPassword.length <6){
            requireContext().toast("Password should not be at least 6 characters")
        }else{
            val pDialog = ClassProgressDialog(thisContext, "Processing Registration...")
            pDialog.createDialog()

            RetrofitConstant.RetrofitConstantGET.create(NYSCRegService::class.java)
                .reg(
                    request_type = "nysc_register",
                    name = agentName,
                    address = latLong.address,
                    email = agentEmail,
                    mobile_no = agentMobileNo,
                    password = agentPassword,
                    state_code = agentStateCode,
                    latitude = "${latLong.lat}",
                    longitude = "${latLong.long}"
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

}


interface NYSCRegService{

    @Multipart
    @POST("reg_nysc.php")
    fun reg(
        @Part("request_type") request_type:String,
        @Part("name") name:String,
        @Part("state_code") state_code:String,
        @Part("address") address:String,
        @Part("latitude") latitude:String,
        @Part("longitude") longitude:String,
        @Part("mobile_no") mobile_no:String,
        @Part("email") email:String,
        @Part("password") password:String
    ):Call<ServerResponse>
}