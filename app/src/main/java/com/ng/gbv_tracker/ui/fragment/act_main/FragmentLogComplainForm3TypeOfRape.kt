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
import com.ng.gbv_tracker.R
import com.ng.gbv_tracker.adapter.AdapterRapeType
import com.ng.gbv_tracker.adapter.RapeTypeClickListener
import com.ng.gbv_tracker.databinding.FragmentLogComplainForm2TypeOfVictimBinding
import com.ng.gbv_tracker.databinding.FragmentLogComplainForm3TypeOfRapeBinding
import com.ng.gbv_tracker.model.RapeDetail
import com.ng.gbv_tracker.viewmodel.RapeComplainFormViewModel


class FragmentLogComplainForm3TypeOfRape : Fragment() {

    lateinit var rapeComplainFormViewModel: RapeComplainFormViewModel
    lateinit var ADAPTER : AdapterRapeType
    lateinit var binding: FragmentLogComplainForm3TypeOfRapeBinding
    lateinit var rapeDetail: RapeDetail



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rapeComplainFormViewModel.allRapeType.observe(viewLifecycleOwner, Observer {
            it?.apply {
                ADAPTER?.rapeTypes = it
            }
        })

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentLogComplainForm3TypeOfRapeBinding.inflate(inflater)

        val application = requireNotNull(activity).application
        binding.lifecycleOwner = this
        rapeDetail = arguments.let { FragmentLogComplainForm3TypeOfRapeArgs.fromBundle(it!!).updatedRapeDetail}
        val viewModelFactory = RapeComplainFormViewModel.Factory(rapeDetail, application)
        rapeComplainFormViewModel = ViewModelProvider(this, viewModelFactory).get(
            RapeComplainFormViewModel::class.java)



        ADAPTER = AdapterRapeType(RapeTypeClickListener {
            rapeDetail.typeOfRape = it.id
            this.findNavController().navigate(FragmentLogComplainForm3TypeOfRapeDirections.actionFragmentLogComplainForm3TypeOfRapeToFragmentLogComplainForm4SelectSupport(rapeDetail))
        })
        binding.recyclerRapeType.apply {
            adapter = ADAPTER
            layoutManager= LinearLayoutManager(activity)
        }

        return binding.root
    }


}