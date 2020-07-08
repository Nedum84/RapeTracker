package com.ng.rapetracker.ui.fragment.act_login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.TextUtils.isEmpty
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.happinesstonic.viewmodel.ModelLoginActivity
import com.ng.rapetracker.R
import com.ng.rapetracker.databinding.FragmentLoginBinding
import com.ng.rapetracker.databinding.FragmentRegisterVictimBinding
import com.ng.rapetracker.network.*
import com.ng.rapetracker.network.RetrofitConstant.Companion.retrofitWithJsonRes
import com.ng.rapetracker.ui.fragment.BaseFragment
import com.ng.rapetracker.utils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FragmentLogin : BaseFragment() {
    lateinit var binding: FragmentLoginBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,xsavedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater)

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        binding.password.transformationMethod = PasswordTransformationMethod()

        binding.goToRegister.setOnClickListener {
            this.findNavController().navigate(FragmentLoginDirections.actionFragmentLoginToFragmentChooseRegType())
        }

        binding.loginBtn.setOnClickListener {

            launch {
                loginUser()
            }
        }
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


                            if (!(serverResponse!!.success as Boolean)){
                                context!!.toast(serverResponse.respMessage!!)
                            }else{
                                //Redirect...
                                val viewModelLoginActivity = ViewModelProvider(this@FragmentLogin).get(
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

}