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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.happinesstonic.viewmodel.ModelHomeActivity
import com.ng.rapetracker.R
import com.ng.rapetracker.adapter.*
import com.ng.rapetracker.databinding.FragmentLogComplainForm2TypeOfVictimBinding
import com.ng.rapetracker.databinding.FragmentMainBinding
import com.ng.rapetracker.model.RapeDetail
import com.ng.rapetracker.model.RapeType
import com.ng.rapetracker.ui.fragment.act_main.FragmentLogComplainForm2TypeOfVictimArgs.fromBundle
import com.ng.rapetracker.utils.ClassAlertDialog
import com.ng.rapetracker.viewmodel.GetRapeDetailViewModel
import com.ng.rapetracker.viewmodel.RapeComplainFormViewModel
import com.ng.rapetracker.viewmodel.factory.GetRapeDetailViewModelFactory


class FragmentLogComplainForm2TypeOfVictim : Fragment() {

    lateinit var rapeComplainFormViewModel: RapeComplainFormViewModel
    lateinit var ADAPTER : AdapterTypeOfVictim
    lateinit var binding: FragmentLogComplainForm2TypeOfVictimBinding
    lateinit var rapeDetail:RapeDetail



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rapeComplainFormViewModel.allRapeTypeOfVictim.observe(viewLifecycleOwner, Observer {
            it?.apply {
                ADAPTER?.rapeTypeOfVictim = it
            }
        })


        rapeComplainFormViewModel.formNextBtn.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { data ->
                if(data){
                    //redirect to the next
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentLogComplainForm2TypeOfVictimBinding.inflate(inflater)

        val application = requireNotNull(activity).application
        binding.lifecycleOwner = this
        rapeDetail = arguments.let { fromBundle(it!!).updatedRapeDetail}
        val viewModelFactory = RapeComplainFormViewModel.Factory(rapeDetail, application)
        rapeComplainFormViewModel = ViewModelProvider(this, viewModelFactory).get(RapeComplainFormViewModel::class.java)


        binding.rapeDetail = rapeDetail

        ADAPTER = AdapterTypeOfVictim(RapeTypeOfVictimClickListener {
            rapeDetail.typeOfVictim = it.typeOfVictim
            this.findNavController().navigate(FragmentLogComplainForm2TypeOfVictimDirections.actionFragmentLogComplainForm2TypeOfVictimToFragmentLogComplainForm3TypeOfRape(rapeDetail))
        })
        binding.recyclerRapeTypeOfVictim.adapter = ADAPTER

        return binding.root
    }


}


class FragmentMain : Fragment() {
    val linearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    lateinit var ADAPTER : AdapterRapeDetail
    lateinit var binding: FragmentMainBinding
    lateinit var getRapeViewModel: GetRapeDetailViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        val application = requireNotNull(this.activity).application
        val viewModelFactory = GetRapeDetailViewModelFactory(application)
        getRapeViewModel = ViewModelProvider(this, viewModelFactory).get(GetRapeDetailViewModel::class.java)

        binding.lifecycleOwner = this


        ADAPTER = AdapterRapeDetail(RapeDetailClickListener {
            ClassAlertDialog(application).toast("Rape Detail Clicked...!!!")
        })
        binding.rapeDetailList.apply {
            adapter = ADAPTER
            layoutManager=linearLayoutManager
        }

        getRapeViewModel.allRapeDetails.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isEmpty()){
                    binding.noComplainWrapper.visibility = View.VISIBLE
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