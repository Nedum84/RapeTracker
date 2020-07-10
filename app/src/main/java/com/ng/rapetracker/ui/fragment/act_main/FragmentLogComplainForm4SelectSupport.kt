package com.ng.rapetracker.ui.fragment.act_main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ng.rapetracker.R
import com.ng.rapetracker.adapter.AdapterRapeSupportOrgType
import com.ng.rapetracker.adapter.AdapterRapeType
import com.ng.rapetracker.adapter.RapeSupportOrgClickListener
import com.ng.rapetracker.adapter.RapeTypeClickListener
import com.ng.rapetracker.databinding.FragmentLogComplainForm3TypeOfRapeBinding
import com.ng.rapetracker.databinding.FragmentLogComplainForm4SelectSupportBinding
import com.ng.rapetracker.model.RapeDetail
import com.ng.rapetracker.viewmodel.RapeComplainFormViewModel

class FragmentLogComplainForm4SelectSupport : Fragment() {

    lateinit var rapeComplainFormViewModel: RapeComplainFormViewModel
    lateinit var ADAPTER : AdapterRapeSupportOrgType
    lateinit var binding: FragmentLogComplainForm4SelectSupportBinding
    lateinit var rapeDetail: RapeDetail



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rapeComplainFormViewModel.allRapeSupportType.observe(viewLifecycleOwner, Observer {
            it?.apply {
                ADAPTER?.rapeSupportTypes = it
            }
        })

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentLogComplainForm4SelectSupportBinding.inflate(inflater)

        val application = requireNotNull(activity).application
        binding.lifecycleOwner = this
        rapeDetail = arguments.let { FragmentLogComplainForm4SelectSupportArgs.fromBundle(it!!).updatedRapeDetail}
        val viewModelFactory = RapeComplainFormViewModel.Factory(rapeDetail, application)
        rapeComplainFormViewModel = ViewModelProvider(this, viewModelFactory).get(
            RapeComplainFormViewModel::class.java)



        ADAPTER = AdapterRapeSupportOrgType(RapeSupportOrgClickListener {
            rapeDetail.rapeSupportType = it.id
            this.findNavController().navigate(FragmentLogComplainForm4SelectSupportDirections.actionFragmentLogComplainForm4SelectSupportToFragmentLogComplainForm5RapeDetail(rapeDetail))
        })
        binding.recyclerRapeSupport.apply {
            adapter = ADAPTER
            layoutManager= LinearLayoutManager(activity)
        }

        return binding.root
    }


}