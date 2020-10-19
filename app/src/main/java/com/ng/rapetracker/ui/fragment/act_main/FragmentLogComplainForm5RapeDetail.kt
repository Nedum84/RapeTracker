package com.ng.rapetracker.ui.fragment.act_main

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
import com.ng.rapetracker.databinding.FragmentLogComplainForm5RapeDetailBinding
import com.ng.rapetracker.model.RapeDetail
import com.ng.rapetracker.model.User
import com.ng.rapetracker.network.*
import com.ng.rapetracker.network.RetrofitConstant.Companion.RetrofitConstantGET
import com.ng.rapetracker.ui.fragment.BaseFragment
import com.ng.rapetracker.utils.ClassAlertDialog
import com.ng.rapetracker.utils.ClassSharedPreferences
import com.ng.rapetracker.utils.toast
import com.ng.rapetracker.viewmodel.ModelNYSCAgent
import com.ng.rapetracker.viewmodel.RapeComplainFormViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentLogComplainForm5RapeDetail : BaseFragment() {
    lateinit var rapeComplainFormViewModel: RapeComplainFormViewModel
    lateinit var binding: FragmentLogComplainForm5RapeDetailBinding
    lateinit var rapeDetail: RapeDetail

    val modelNYSCAgent by lazy { requireActivity().run{ViewModelProvider(this, ModelNYSCAgent.Factory(application)).get(ModelNYSCAgent::class.java)}}



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.selectNyscAgent.setOnClickListener {
            val frag = FragmentLogComplainForm6NYSCAgent()
            requireActivity().let {
                frag.apply {
                    show(it.supportFragmentManager, tag)
                }
            }
        }



        modelNYSCAgent.curItemCategory.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled().let {
                it?.let {
                    rapeDetail.nyscAgentId = it.agent_id
                    binding.nyscAgent.text = it.name
                }

                if (it==null&&rapeDetail.nyscAgentId!=0){
                    launch {
                        val nyscAgent = db.nYSCagentDao.getById(rapeDetail.nyscAgentId.toLong())
                        withContext(Dispatchers.Main){
                            modelNYSCAgent.setItemCategory(nyscAgent!!)
                        }
                    }
                }
            }
        })

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentLogComplainForm5RapeDetailBinding.inflate(inflater)

        binding.lifecycleOwner = this
        rapeDetail = arguments.let { FragmentLogComplainForm4SelectSupportArgs.fromBundle(it!!).updatedRapeDetail}
        val viewModelFactory = RapeComplainFormViewModel.Factory(rapeDetail, application)
        rapeComplainFormViewModel = ViewModelProvider(this, viewModelFactory).get(
            RapeComplainFormViewModel::class.java)

        binding.nextBtn.setOnClickListener {
            val rapeAddress = binding.rapeAddress.text.trim().toString()
            val rapeDetails = binding.rapeDetails.text.trim().toString()

            if (TextUtils.isEmpty(rapeAddress) || TextUtils.isEmpty(rapeDetails)) {
                context.let { it!!.toast("Enter the address and the details of the sexual abuse") }
            } else if (rapeDetail.nyscAgentId==0){
                context.let { it!!.toast("Select NYSC closest to you") }
            }else {
                rapeDetail.rapeAddress = rapeAddress
                rapeDetail.rapeDescription = rapeDetails

                this.findNavController().navigate(FragmentLogComplainForm5RapeDetailDirections.actionFragmentLogComplainForm5RapeDetailToFragmentLogComplainForm7Details(rapeDetail))
            }
        }


        return binding.root
    }


}