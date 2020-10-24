package com.ng.gbv_tracker.ui.fragment.act_main

import android.app.Application
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.ng.gbv_tracker.databinding.FragmentLogComplainForm5RapeDetailBinding
import com.ng.gbv_tracker.databinding.FragmentLogComplainForm7DetailsBinding
import com.ng.gbv_tracker.model.RapeDetail
import com.ng.gbv_tracker.model.User
import com.ng.gbv_tracker.network.*
import com.ng.gbv_tracker.network.RetrofitConstant.Companion.RetrofitConstantGET
import com.ng.gbv_tracker.ui.fragment.BaseFragment
import com.ng.gbv_tracker.utils.ClassAlertDialog
import com.ng.gbv_tracker.utils.ClassProgressDialog
import com.ng.gbv_tracker.utils.ClassSharedPreferences
import com.ng.gbv_tracker.utils.toast
import com.ng.gbv_tracker.viewmodel.ModelNYSCAgent
import com.ng.gbv_tracker.viewmodel.RapeComplainFormViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentLogComplainForm7Details : BaseFragment() {

    lateinit var rapeComplainFormViewModel: RapeComplainFormViewModel
    lateinit var binding: FragmentLogComplainForm7DetailsBinding
    lateinit var rapeDetail: RapeDetail



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rapeComplainFormViewModel.rapeComplainLogSuccessful.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { data ->
                if(data){
                    //redirect to home
                    this.findNavController().navigate(FragmentLogComplainForm7DetailsDirections.actionFragmentLogComplainForm7DetailsToFragmentMain())
                }
            }
        })


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentLogComplainForm7DetailsBinding.inflate(inflater)

        binding.lifecycleOwner = this
        rapeDetail = arguments.let { FragmentLogComplainForm4SelectSupportArgs.fromBundle(it!!).updatedRapeDetail}
        val viewModelFactory = RapeComplainFormViewModel.Factory(rapeDetail, application)
        rapeComplainFormViewModel = ViewModelProvider(this, viewModelFactory).get(
            RapeComplainFormViewModel::class.java)


        launch {
            binding.rapeReporter.text = rapeDetail.userName

            val typeOfVictim = db.rapeTypeOfVictimDao.getRapeTypeOfVictimById(rapeDetail.typeOfVictim.toLong())
            binding.rapeVictim.text = typeOfVictim!!.typeOfVictim

            val rapeType = db.rapeTypeDao.getRapeTypeById(rapeDetail.typeOfRape.toLong())
            binding.typeOfRape.text = rapeType!!.rapeType


            val rapeSupportType = db.rapeSupportTypeDao.getRapeSupportById(rapeDetail.rapeSupportType)
            binding.typeOfSupport.text = rapeSupportType!!.rapeSupportType


            binding.addressOfOccurrence.text = rapeDetail.rapeAddress
            binding.rapeDesc.text = rapeDetail.rapeDescription

            val nyscAgent = db.nYSCagentDao.getById(rapeDetail.nyscAgentId.toLong())
            binding.nyscAgent.text = "${nyscAgent!!.name}/${nyscAgent!!.mobile_no}"
        }



        binding.rapeLogComplainBtn.setOnClickListener {
            if(ClassSharedPreferences(requireContext()).getAccessLevel()==2){
                ClassAlertDialog(requireActivity()).alertMessage("You cannot Log Rape Complain because you didn't register as a GBV-Victim")
                return@setOnClickListener
            }


            launch {
                logComplain()
            }
        }
        binding.rapeCancelLogBtn.setOnClickListener {
            this.findNavController().navigate(FragmentLogComplainForm7DetailsDirections.actionFragmentLogComplainForm7DetailsToFragmentMain())
        }


        return binding.root
    }

    private suspend fun logComplain(){
        val pDialog = ClassProgressDialog(thisContext, "Login you In...")
        pDialog.createDialog()

        val userDetails = Gson().fromJson(prefs.getCurUserDetail(), User::class.java)
        rapeDetail.userId = userDetails.id
        rapeDetail.userName = userDetails.userName
        rapeDetail.userAge = userDetails.userAge

        val logComplainService = RetrofitConstantGET.create(LogRapeComplainService::class.java)
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
            "${rapeDetail.dateAdded}",
            "${rapeDetail.nyscAgentId}"
        ).enqueue(object: Callback<ServerResponse> {
            override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                pDialog.dismissDialog()
                requireContext().toast("No internet connect!")
            }

            override fun onResponse(call: Call<ServerResponse>, response: Response<ServerResponse>) {
                pDialog.dismissDialog()
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