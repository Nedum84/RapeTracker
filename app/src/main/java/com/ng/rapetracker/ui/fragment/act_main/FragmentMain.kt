package com.ng.rapetracker.ui.fragment.act_main

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
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
import com.ng.rapetracker.model.Organization
import com.ng.rapetracker.model.RapeSupportType
import com.ng.rapetracker.model.User
import com.ng.rapetracker.room.DatabaseRoom
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


class FragmentMain : Fragment() {
    lateinit var prefs:ClassSharedPreferences
    lateinit var thisContext:Activity

    lateinit var ADAPTER : AdapterRapeDetail
    lateinit var binding:FragmentMainBinding
    lateinit var getRapeViewModel:GetRapeDetailViewModel
    lateinit var modelMainActivity: ModelMainActivity
    lateinit var databaseRoom: DatabaseRoom
    lateinit var orgDetail:Organization
    lateinit var userDetail:User


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        val application = requireNotNull(this.activity).application
        val viewModelFactory = GetRapeDetailViewModelFactory(application)
        getRapeViewModel = ViewModelProvider(this, viewModelFactory).get(GetRapeDetailViewModel::class.java)
        thisContext = requireActivity()
        prefs = ClassSharedPreferences(thisContext)

        databaseRoom = DatabaseRoom.getDatabaseInstance(application)
        binding.lifecycleOwner = this



        if (prefs.getAccessLevel() == 1){
            userDetail = Gson().fromJson(prefs.getCurUserDetail(), User::class.java)
            binding.titleShow.text = "...My Rape Complains..."
        }else if (prefs.getAccessLevel() == 2){
            orgDetail = Gson().fromJson(prefs.getCurOrgDetail(), Organization::class.java)



            CoroutineScope((Dispatchers.Default)).launch {
                val org = databaseRoom.rapeSupportTypeDao.getRapeSupportById(orgDetail.orgType)
                binding.titleShow.text = "\"${org!!.rapeSupportType}\" Complains"
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
                    }else{
                        binding.noComplainForSupport.visibility = View.VISIBLE
                        CoroutineScope((Dispatchers.Default)).launch {
                            val org = databaseRoom.rapeSupportTypeDao.getRapeSupportById(orgDetail.orgType)
                            withContext(Dispatchers.Main){
                                binding.noComplainTitleShowSupport.text = "No complain Logged for ${org!!.rapeSupportType} at this Moment"
                            }
                        }
                    }
                }else{
                    ADAPTER.addNewItems(it)
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