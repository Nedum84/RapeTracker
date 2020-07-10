package com.ng.rapetracker.ui.fragment.act_main

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ng.rapetracker.R
import com.ng.rapetracker.adapter.AdapterRapeDetail
import com.ng.rapetracker.adapter.RapeDetailClickListener
import com.ng.rapetracker.databinding.FragmentMainBinding
import com.ng.rapetracker.utils.ClassAlertDialog
import com.ng.rapetracker.utils.ClassSharedPreferences
import com.ng.rapetracker.viewmodel.GetRapeDetailViewModel
import com.ng.rapetracker.viewmodel.factory.GetRapeDetailViewModelFactory


class FragmentMain : Fragment() {
    lateinit var prefs:ClassSharedPreferences
    lateinit var thisContext:Activity

    lateinit var ADAPTER : AdapterRapeDetail
    lateinit var binding:FragmentMainBinding
    lateinit var getRapeViewModel:GetRapeDetailViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        val application = requireNotNull(this.activity).application
        val viewModelFactory = GetRapeDetailViewModelFactory(application)
        getRapeViewModel = ViewModelProvider(this, viewModelFactory).get(GetRapeDetailViewModel::class.java)
        thisContext = requireActivity()
        prefs = ClassSharedPreferences(thisContext)

        binding.lifecycleOwner = this


        ADAPTER = AdapterRapeDetail(RapeDetailClickListener {
            ClassAlertDialog(application).toast("Rape Detail Clicked...!!!")
        })
        binding.rapeDetailList.apply {
            adapter = ADAPTER
            layoutManager=LinearLayoutManager(activity)
        }

        getRapeViewModel.allRapeDetails.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isEmpty()){
                    if (prefs.getAccessLevel()==1)
                        binding.noComplainWrapper.visibility = View.VISIBLE
                    else
                        binding.noComplainForSupport.visibility = View.VISIBLE
                }else{
                    ADAPTER.addNewItems(it)
                }
                ClassAlertDialog(application).toast("refresh from Main Frag ... 1st observer...!!!")
            }
        })


        getRapeViewModel.allRapeDetails2.observe(viewLifecycleOwner, Observer {
            ClassAlertDialog(application).toast("refresh from Main Frag ... second observer...!!!")
        })


        binding.logComplainBtn.setOnClickListener {
            this.findNavController().navigate(FragmentMainDirections.actionFragmentMainToFragmentLogComplainForm1RapeVictim())
        }




        return binding.root
    }


}