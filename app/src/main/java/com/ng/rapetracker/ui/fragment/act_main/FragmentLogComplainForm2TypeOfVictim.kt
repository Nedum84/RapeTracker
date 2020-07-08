package com.ng.rapetracker.ui.fragment.act_main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.happinesstonic.viewmodel.ModelHomeActivity
import com.ng.rapetracker.R
import com.ng.rapetracker.adapter.AdapterRapeDetail
import com.ng.rapetracker.adapter.AdapterRapeType
import com.ng.rapetracker.adapter.RapeDetailClickListener
import com.ng.rapetracker.adapter.RapeTypeClickListener
import com.ng.rapetracker.model.RapeType
import com.ng.rapetracker.viewmodel.RapeComplainFormViewModel


class FragmentLogComplainForm2TypeOfVictim : Fragment() {

    lateinit var rapeComplainFormViewModel: RapeComplainFormViewModel
    lateinit var ADAPTER : AdapterRapeType


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val application = requireNotNull(activity).application
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        val marsProperty = DetailFragmentArgs.fromBundle(arguments!!).selectedProperty
        val viewModelFactory = RapeComplainFormViewModel.Factory(marsProperty, application)
        rapeComplainFormViewModel = ViewModelProvider(this, viewModelFactory).get(RapeComplainFormViewModel::class.java)


        binding.viewModel = rapeComplainFormViewModel


        //OR
        ADAPTER = AdapterRapeType(RapeTypeClickListener {
            // Must find the NavController from the Fragment
            this.findNavController().navigate(OverviewFragmentDirections.actionShowDetail(it))
            // Tell the ViewModel we've made the navigate call to prevent multiple navigation
            viewModel.displayPropertyDetailsComplete()
        })
        binding.photosGrid.adapter = ADAPTER

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rapeComplainFormViewModel.allRapeType.observe(viewLifecycleOwner, Observer {
            it?.apply {
                ADAPTER?.rapeTypes = it
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_log_complain_form2_type_of_victim,
            container,
            false
        )
    }


}