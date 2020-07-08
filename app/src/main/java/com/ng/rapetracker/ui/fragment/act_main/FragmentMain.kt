package com.ng.rapetracker.ui.fragment.act_main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.happinesstonic.viewmodel.ModelHomeActivity
import com.ng.rapetracker.R
import com.ng.rapetracker.adapter.AdapterRapeDetail
import com.ng.rapetracker.adapter.RapeDetailClickListener
import com.ng.rapetracker.model.RapeDetail
import com.ng.rapetracker.utils.ClassAlertDialog
import com.ng.rapetracker.viewmodel.GetRapeDetailViewModel
import com.ng.rapetracker.viewmodel.RapeComplainFormViewModel
import com.ng.rapetracker.viewmodel.factory.GetRapeDetailViewModelFactory


class FragmentMain : Fragment() {

    val linearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    lateinit var ADAPTER : AdapterTextJoke



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: FragmentSleepTrackerBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sleep_tracker, container, false)

        val application = requireNotNull(this.activity).application

        val viewModelFactory = GetRapeDetailViewModelFactory(application)

        val getRapeViewModel = ViewModelProvider(this, viewModelFactory).get(GetRapeDetailViewModel::class.java)

        binding.sleepTrackerViewModel = getRapeViewModel

        binding.setLifecycleOwner(this)


        val adapter = AdapterRapeDetail(RapeDetailClickListener {
            ClassAlertDialog(application).toast("Rape Detail Clicked...!!!")
        })
        binding.sleepList.adapter = adapter


        getRapeViewModel.allRapeDetails.observe(this, Observer {
            it?.let {
                adapter.addNewItems(it)
            }
        })


        getRapeViewModel.allRapeDetails2.observe(this, Observer {
            ClassAlertDialog(application).toast("refresh from Main Frag ... second observer...!!!")
        })







    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }


}