package com.ng.gbv_tracker.ui.fragment.act_main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ng.gbv_tracker.adapter.*
import com.ng.gbv_tracker.databinding.FragmentLogComplainForm2TypeOfVictimBinding
import com.ng.gbv_tracker.model.RapeDetail
import com.ng.gbv_tracker.ui.fragment.act_main.FragmentLogComplainForm2TypeOfVictimArgs.fromBundle
import com.ng.gbv_tracker.viewmodel.RapeComplainFormViewModel


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
            rapeDetail.typeOfVictim = it.id
            this.findNavController().navigate(FragmentLogComplainForm2TypeOfVictimDirections.actionFragmentLogComplainForm2TypeOfVictimToFragmentLogComplainForm3TypeOfRape(rapeDetail))
        })
        binding.recyclerRapeTypeOfVictim.apply {
            adapter = ADAPTER
            layoutManager=LinearLayoutManager(activity)
        }

        return binding.root
    }


}
