package com.ng.rapetracker.ui.fragment.act_login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.ng.rapetracker.R
import com.ng.rapetracker.databinding.FragmentRegisterOrgDetailBinding
import com.ng.rapetracker.ui.fragment.BaseFragment
import kotlinx.coroutines.launch


class FragmentRegisterOrgDetail : BaseFragment() {
    lateinit var binding: FragmentRegisterOrgDetailBinding
    private lateinit var thisContext: Activity

    private lateinit var selectedGenderName: String
    private lateinit var selectedGenderId: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Get a reference to the binding object and inflate the fragment views.
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_org_detail, container, false)
        thisContext = requireActivity()




        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        binding.userPassword.transformationMethod = PasswordTransformationMethod()
        genderSpinnerInitialize()

        binding.goToLogin.setOnClickListener {
            this.findNavController().navigate(FragmentRegisterVictimDirections.actionFragmentRegisterVictimToFragmentLogin())
        }
        binding.regAsRapeSupport.setOnClickListener {
            this.findNavController().navigate(FragmentRegisterVictimDirections.actionFragmentRegisterVictimToFragmentRegisterOrgSupportType())
        }


        binding.registerBtn.setOnClickListener {

            launch {
                registerSupportOrg()
            }
        }
    }

    private suspend fun registerSupportOrg(){
        val userName = binding.userName.text.trim().toString()
        val userMobileNo = binding.userMobileNo.text.trim().toString()
        val userEmail = binding.userEmail.text.trim().toString()
        val userAge = binding.userAge.text.trim().toString()
        val userCountry = binding.userCountry.text.trim().toString()
//        val userGender = binding.userGender.text.trim().toString()
        val userGender = selectedGenderId
        val userState = binding.userState.text.trim().toString()
        val userAddress = binding.userAddress.text.trim().toString()
        val userPassword = binding.userPassword.text.trim().toString()

        if (isEmpty(userName) ||isEmpty(userMobileNo) ||isEmpty(userEmail) ||isEmpty(userAge) ||isEmpty(userCountry) || isEmpty(userState) ||isEmpty(userAddress) ||isEmpty(userPassword)){
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
                                coroutine.launch {
                                    redirect()
                                }
                            }
                        }
                    } else {
                        context!!.toast("An error occurred, Try again")
                    }
                }

            })
        }
    }

    suspend fun redirect(){
        withContext(Dispatchers.Main) {
            val viewModelLoginActivity = ViewModelProvider(this@FragmentRegisterVictim).get(ModelLoginActivity::class.java)
            viewModelLoginActivity.setGotoMainActivity(true)
        }
    }
    private fun genderSpinnerInitialize(){
        val genderNameArray = arrayListOf("Gender", "Male", "Female")
        val genderIdArray = arrayListOf("-1", "1", "2")


        val subjectSpinnerArrayAdapter = ArrayAdapter(thisContext, android.R.layout.simple_spinner_dropdown_item, genderNameArray)
        //selected item will look like a spinner set from XML
        subjectSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.userGender?.adapter = subjectSpinnerArrayAdapter

        binding.userGender?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedGenderId = genderIdArray[position]
                selectedGenderName = genderNameArray[position]
            }

        }


    }
}