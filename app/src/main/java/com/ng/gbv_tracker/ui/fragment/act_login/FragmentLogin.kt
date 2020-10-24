package com.ng.gbv_tracker.ui.fragment.act_login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ng.gbv_tracker.databinding.FragmentLoginBinding
import com.ng.gbv_tracker.network.LoginRegService
import com.ng.gbv_tracker.network.RetrofitConstant.Companion.RetrofitConstantGET
import com.ng.gbv_tracker.network.ServerResponse
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



class FragmentLogin : BaseFragment() {
    lateinit var binding: FragmentLoginBinding
    lateinit var viewModelLoginActivity:ModelLoginActivity


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,xsavedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater)

//        val viewModelFactory = ModelLoginActivity.Factory(appCtx)
//        viewModelLoginActivity = ViewModelProvider(this, viewModelFactory).get(
//            ModelLoginActivity::class.java)

        binding.password.transformationMethod = PasswordTransformationMethod()

//        binding.goToRegister.setOnClickListener {
////            this.findNavController().navigate(FragmentLoginDirections.actionFragmentLoginToFragmentChooseRegType())
//        }

        binding.loginBtn.setOnClickListener {

            launch {
                loginUser()
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
    }



    private fun loginUser(){
        val emailMobileNo = binding.emailMobileNo.text.trim().toString()
        val password = binding.password.text.trim().toString()

        if (isEmpty(emailMobileNo) || isEmpty(password)){
            context.let {it!!.toast("Enter both fields")}
        }else{
            val pDialog = ClassProgressDialog(thisContext, "Login you In...")
            pDialog.createDialog()

            val loginService = RetrofitConstantGET.create(LoginRegService::class.java)
            loginService.loginRequest(
                "login",
                emailMobileNo,
                password
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
                                        context!!.toast("Login successful...")


                                        //Save and Redirect...
                                        viewModelLoginActivity.saveUserDetails(obj)

                                        activity!!.let {
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
                        Log.d("loggin__", "$response")
                        context!!.toast("An error occurred, Try again")
                    }
                }

            })
        }
    }

}