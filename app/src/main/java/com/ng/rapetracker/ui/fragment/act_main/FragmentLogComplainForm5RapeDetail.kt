package com.ng.rapetracker.ui.fragment.act_main

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.ng.rapetracker.R
import com.ng.rapetracker.adapter.AdapterRapeSupportOrgType
import com.ng.rapetracker.adapter.RapeSupportOrgClickListener
import com.ng.rapetracker.databinding.FragmentLogComplainForm4SelectSupportBinding
import com.ng.rapetracker.databinding.FragmentLogComplainForm5RapeDetailBinding
import com.ng.rapetracker.model.Organization
import com.ng.rapetracker.model.RapeDetail
import com.ng.rapetracker.model.User
import com.ng.rapetracker.network.*
import com.ng.rapetracker.network.RetrofitConstant.Companion.retrofitWithJsonRes
import com.ng.rapetracker.ui.fragment.BaseFragment
import com.ng.rapetracker.utils.ClassSharedPreferences
import com.ng.rapetracker.utils.toast
import com.ng.rapetracker.viewmodel.RapeComplainFormViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentLogComplainForm5RapeDetail : BaseFragment() {
    lateinit var appCtx:Application

    lateinit var rapeComplainFormViewModel: RapeComplainFormViewModel
    lateinit var binding: FragmentLogComplainForm5RapeDetailBinding
    lateinit var rapeDetail: RapeDetail



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rapeComplainFormViewModel.rapeComplainLogSuccessful.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { data ->
                if(data){
                    //redirect to home
                    this.findNavController().navigate(FragmentLogComplainForm5RapeDetailDirections.actionFragmentLogComplainForm5RapeDetailToFragmentMain())
                }
            }
        })

        binding.rapeCancelLogBtn.setOnClickListener {
            this.findNavController().navigate(FragmentLogComplainForm5RapeDetailDirections.actionFragmentLogComplainForm5RapeDetailToFragmentMain())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentLogComplainForm5RapeDetailBinding.inflate(inflater)

        appCtx = requireNotNull(activity).application
        binding.lifecycleOwner = this
        rapeDetail = arguments.let { FragmentLogComplainForm4SelectSupportArgs.fromBundle(it!!).updatedRapeDetail}
        val viewModelFactory = RapeComplainFormViewModel.Factory(rapeDetail, appCtx)
        rapeComplainFormViewModel = ViewModelProvider(this, viewModelFactory).get(
            RapeComplainFormViewModel::class.java)

        binding.rapeLogComplainBtn.setOnClickListener {

            launch {
                logComplain()
            }
        }


        return binding.root
    }

    private suspend fun logComplain(){
        val rapeAddress = binding.rapeAddress.text.trim().toString()
        val rapeDetails = binding.rapeDetails.text.trim().toString()

        if (TextUtils.isEmpty(rapeAddress) || TextUtils.isEmpty(rapeDetails)){
            context.let {it!!.toast("Enter the address and the details of the sexual abuse")}
        }else{

            val prefs = ClassSharedPreferences(appCtx)

            val userDetails = Gson().fromJson(prefs.getCurUserDetail(), User::class.java)
            rapeDetail.userId = userDetails.id
            rapeDetail.userName = userDetails.userName
            rapeDetail.userAge = userDetails.userAge
            rapeDetail.rapeAddress = rapeAddress
            rapeDetail.rapeDescription = rapeDetails

            val logComplainService = retrofitWithJsonRes.create(LogRapeComplainService::class.java)
            logComplainService.rapeComplainRequest(
                "log_rape_complain",
                "${rapeDetail.rapeAgainstYou}",
                "${rapeDetail.typeOfVictim}",
                "${rapeDetail.typeOfRape}",
                "${rapeDetail.rapeSupportType}",
                "${rapeDetail.rapeAddress}",
                "${rapeDetail.rapeDescription}",
                "${rapeDetail.userId}",
                "${rapeDetail.userName}",
                "${rapeDetail.userAge}",
                "${rapeDetail.dateAdded}"
            ).enqueue(object: Callback<ServerResponse> {
                override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                    requireContext().toast("No internet connect!")
                }

                override fun onResponse(call: Call<ServerResponse>, response: Response<ServerResponse>) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {

                            val serverResponse = response.body()


                            if (!(serverResponse!!.success as Boolean)){
                                context!!.toast(serverResponse.respMessage!!)
                            }else{
                                //Redirect...
                                rapeComplainFormViewModel.setRapeComplainLogSuccessful(true)
                                context!!.toast("Complain logged Successfully...")
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