package com.ng.rapetracker.ui.fragment.act_main

import android.app.Activity
import android.content.Context
import android.content.Context.*
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.ng.rapetracker.R
import com.ng.rapetracker.adapter.AdapterRapeDetail
import com.ng.rapetracker.adapter.RapeDetailClickListener
import com.ng.rapetracker.databinding.FragmentMainBinding
import com.ng.rapetracker.model.NYSCagent
import com.ng.rapetracker.model.Organization
import com.ng.rapetracker.model.RapeSupportType
import com.ng.rapetracker.model.User
import com.ng.rapetracker.room.DatabaseRoom
import com.ng.rapetracker.ui.fragment.BaseFragment
import com.ng.rapetracker.utils.ClassAlertDialog
import com.ng.rapetracker.utils.ClassSharedPreferences
import com.ng.rapetracker.viewmodel.GetRapeDetailViewModel
import com.ng.rapetracker.viewmodel.ModelMainActivity
import com.ng.rapetracker.viewmodel.factory.GetRapeDetailViewModelFactory
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
                binding.titleShow.text = "...My Rape Complains..."
            }
            2 -> {nysCagent = Gson().fromJson(prefs.getCurNYSCAgent(), NYSCagent::class.java)}
            3 -> {
                orgDetail = Gson().fromJson(prefs.getCurOrgDetail(), Organization::class.java)



                CoroutineScope((Dispatchers.Default)).launch {
                    val org = databaseRoom.rapeSupportTypeDao.getRapeSupportById(orgDetail.orgType)
                    binding.titleShow.text = "\"${org!!.rapeSupportType}\" Complains"
                }
            }
        }



        ADAPTER = AdapterRapeDetail(requireActivity().application, RapeDetailClickListener {
//            ClassAlertDialog(application).toast("Rape Detail Clicked...!!!")
            this.findNavController().navigate(FragmentMainDirections.actionFragmentMainToFragmentRapeDetail(it))
        })
        binding.rapeDetailList.apply {
            adapter = ADAPTER
            layoutManager=LinearLayoutManager(activity)
        }

        getRapeViewModel.allRapeDetails.observe(viewLifecycleOwner, Observer {
            it?.let {

                if (it.isEmpty()){
                    if (prefs.getAccessLevel()==1){
                        binding.noComplainWrapper.visibility = View.VISIBLE
                    }else if (prefs.getAccessLevel()==2){
                        binding.noComplainForSupport.visibility = View.VISIBLE
                        binding.noComplainTitleShowSupport.text = "NYSC member login at this Moment ${nysCagent.name}"
                    }else{
                        binding.noComplainForSupport.visibility = View.VISIBLE
                        CoroutineScope((Dispatchers.Default)).launch {
                            val org = databaseRoom.rapeSupportTypeDao.getRapeSupportById(orgDetail.orgType)
                            withContext(Dispatchers.Main){
//                                binding.noComplainTitleShowSupport.text = "No complain Logged for ${org!!.rapeSupportType} at this Moment"
                            }
                        }
                    }
                }else{

                    activity?.let{act->
                        act.runOnUiThread {
//                            ADAPTER.addNewItems(it)
                        }
                    }
//                    binding.noComplainWrapper.visibility = View.GONE
                }
            }
        })



        binding.logComplainBtn.setOnClickListener {
            this.findNavController().navigate(FragmentMainDirections.actionFragmentMainToFragmentLogComplainForm1RapeVictim())
        }



        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getRapeViewModel.allRapeDetails.observe(viewLifecycleOwner, Observer {
            it?.apply {
//                ClassAlertDialog(thisContext).alertMessage(Gson().toJson(it))
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