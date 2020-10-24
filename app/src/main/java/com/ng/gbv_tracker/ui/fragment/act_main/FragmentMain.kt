package com.ng.gbv_tracker.ui.fragment.act_main

import android.app.Activity
import android.content.Context
import android.content.Context.*
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.ng.gbv_tracker.R
import com.ng.gbv_tracker.adapter.AdapterRapeDetail
import com.ng.gbv_tracker.adapter.RapeDetailClickListener
import com.ng.gbv_tracker.databinding.FragmentMainBinding
import com.ng.gbv_tracker.model.NYSCagent
import com.ng.gbv_tracker.model.Organization
import com.ng.gbv_tracker.model.RapeSupportType
import com.ng.gbv_tracker.model.User
import com.ng.gbv_tracker.room.DatabaseRoom
import com.ng.gbv_tracker.ui.fragment.BaseFragment
import com.ng.gbv_tracker.utils.ClassAlertDialog
import com.ng.gbv_tracker.utils.ClassSharedPreferences
import com.ng.gbv_tracker.viewmodel.GetRapeDetailViewModel
import com.ng.gbv_tracker.viewmodel.ModelMainActivity
import com.ng.gbv_tracker.viewmodel.factory.GetRapeDetailViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class FragmentMain : BaseFragment() {
    lateinit var ADAPTER : AdapterRapeDetail
    lateinit var binding:FragmentMainBinding
    lateinit var getRapeViewModel:GetRapeDetailViewModel
    lateinit var modelMainActivity: ModelMainActivity
    val databaseRoom: DatabaseRoom by lazy { DatabaseRoom.getDatabaseInstance(thisContext.application) }
    lateinit var orgDetail:Organization
    lateinit var userDetail:User
    lateinit var nysCagent: NYSCagent


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        val application = requireNotNull(this.activity).application
        val viewModelFactory = GetRapeDetailViewModelFactory(application)
        getRapeViewModel = ViewModelProvider(this, viewModelFactory).get(GetRapeDetailViewModel::class.java)

        binding.lifecycleOwner = this



        when (prefs.getAccessLevel()){
             1 -> {
                userDetail = Gson().fromJson(prefs.getCurUserDetail(), User::class.java)
                binding.nameOfLoggedInUser.text = "Hi, ${userDetail.userName}"
                 binding.myAddress.text = userDetail.userAddress
            }
            2 -> {
                nysCagent = Gson().fromJson(prefs.getCurNYSCAgent(), NYSCagent::class.java)
                binding.nameOfLoggedInUser.text = "Hi, ${nysCagent.name} (NYSC)"
                binding.myComplaintTitle.text = "COMPLAINTS SUBMITTED TO YOU"
                binding.myAddress.text = nysCagent.address
            }
            3 -> {
                orgDetail = Gson().fromJson(prefs.getCurOrgDetail(), Organization::class.java)



//                CoroutineScope((Dispatchers.Default)).launch {
//                    val org = databaseRoom.rapeSupportTypeDao.getRapeSupportById(orgDetail.orgType)
//                    binding.titleShow.text = "\"${org!!.rapeSupportType}\" Complains"
//                }
            }
        }



        ADAPTER = AdapterRapeDetail(requireActivity().application, RapeDetailClickListener {
//            ClassAlertDialog(application).toast("Rape Detail Clicked...!!!")
            this.findNavController().navigate(FragmentMainDirections.actionFragmentMainToFragmentRapeDetail(it))
        })
        binding.rapeDetailList.apply {
            adapter = ADAPTER
            layoutManager=LinearLayoutManager(activity)
            isNestedScrollingEnabled = false
        }

        getRapeViewModel.allRapeDetails.observe(viewLifecycleOwner, Observer {
            it?.let {

                if (it.isEmpty()){
                    binding.myComplaintTitle.visibility = View.GONE

                    if (prefs.getAccessLevel()==1){
                        binding.noComplainWrapper.visibility = View.VISIBLE
                    }else if (prefs.getAccessLevel()==2){
                        binding.noComplainForSupport.visibility = View.VISIBLE
                    }else{
                        binding.noComplainForSupport.visibility = View.VISIBLE
                    }
                }else{

                    activity?.let{act->
                        act.runOnUiThread {
//                            ADAPTER.addNewItems(it)
                        }
                    }

                    binding.noComplainWrapper.visibility = View.GONE//Users
                    binding.noComplainForSupport.visibility = View.GONE//NYSC

                    binding.myComplaintTitle.visibility = View.VISIBLE//Show title
                }
            }
        })


        binding.logComplainBtn.setOnClickListener {
            this.findNavController().navigate(FragmentMainDirections.actionFragmentMainToFragmentLogComplainForm1RapeVictim())
        }

        binding.viewSarcs1.setOnClickListener {
            requireActivity().run {
                FragmentNYSCToSupportReport().show(this.supportFragmentManager, tag)
            }
        }
        binding.viewSarcs2.setOnClickListener {
            requireActivity().run {
                FragmentNYSCToSupportReport().show(this.supportFragmentManager, tag)
            }
        }



        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getRapeViewModel.allRapeDetails.observe(viewLifecycleOwner, Observer {
            it?.apply {
                ADAPTER.addNewItems(it)
            }
        })


        //Home/Main activity LifeCycle
        modelMainActivity = requireActivity().run{
            ViewModelProvider(this).get(ModelMainActivity::class.java)
        }
        modelMainActivity.gotoFormDialog.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { nav_to ->
                when(nav_to){
                    "form"->{
                        this.findNavController().navigate(FragmentMainDirections.actionFragmentMainToFragmentLogComplainForm1RapeVictim())
                    }
                    "profile"->{

                    }
                    "refresh"->{

                    }
                }
            }
        })
    }



}