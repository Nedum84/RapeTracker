package com.ng.rapetracker.ui.fragment.act_login

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ng.rapetracker.viewmodel.ModelLoginActivity
import com.ng.rapetracker.databinding.FragmentLoginBinding
import com.ng.rapetracker.network.*
import com.ng.rapetracker.network.RetrofitConstant.Companion.retrofitWithJsonRes
import com.ng.rapetracker.room.DatabaseRoom
import com.ng.rapetracker.ui.activity.MainActivity
import com.ng.rapetracker.ui.fragment.BaseFragment
import com.ng.rapetracker.utils.ClassSharedPreferences
import com.ng.rapetracker.utils.toast
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class FragmentLogin : BaseFragment() {
    lateinit var binding: FragmentLoginBinding
    lateinit var thisContext:Activity
    lateinit var appCtx: Application
    lateinit var viewModelLoginActivity:ModelLoginActivity


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,xsavedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        thisContext = requireActivity()
        appCtx= requireNotNull(activity).application

        val viewModelFactory = ModelLoginActivity.Factory(appCtx)
        viewModelLoginActivity = ViewModelProvider(this, viewModelFactory).get(
            ModelLoginActivity::class.java)

        binding.password.transformationMethod = PasswordTransformationMethod()

        binding.goToRegister.setOnClickListener {
            this.findNavController().navigate(FragmentLoginDirections.actionFragmentLoginToFragmentChooseRegType())
        }

        binding.loginBtn.setOnClickListener {

            launch {
                loginUser()
            }
        }


        return binding.root
    }



    private fun loginUser(){
        val emailMobileNo = binding.emailMobileNo.text.trim().toString()
        val password = binding.password.text.trim().toString()

        if (isEmpty(emailMobileNo) || isEmpty(password)){
            context.let {it!!.toast("Enter both fields")}
        }else{

            val loginService = retrofitWithJsonRes.create(LoginRegService::class.java)
            loginService.loginRequest(
                "login",
                emailMobileNo,
                password
            ).enqueue(object: Callback<ServerResponse> {
                override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                    requireContext().toast("No internet connect!")
                }

                override fun onResponse(call: Call<ServerResponse>,response: Response<ServerResponse>) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {

                            val serverResponse = response.body()


                            try {
                                val obj = JSONObject(serverResponse!!.otherDetail!!)
                                if ((serverResponse.success as Boolean)){
                                    if (serverResponse.respMessage == "ok") {
                                        context!!.toast("Login successful...")


                                        //Save and Redirect...
                                        viewModelLoginActivity.saveUserDetails(obj, ClassSharedPreferences(thisContext))

                                        startActivity(Intent(activity!!, MainActivity::class.java))
                                        activity!!.finish()
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