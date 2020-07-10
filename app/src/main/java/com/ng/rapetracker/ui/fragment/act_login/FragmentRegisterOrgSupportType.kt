package com.ng.rapetracker.ui.fragment.act_login

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ng.rapetracker.R
import com.ng.rapetracker.adapter.AdapterRapeSupportOrgType
import com.ng.rapetracker.adapter.RapeSupportOrgClickListener
import com.ng.rapetracker.databinding.FragmentRegisterOrgSupportTypeBinding
import com.ng.rapetracker.ui.fragment.BaseFragment
import com.ng.rapetracker.utils.ClassAlertDialog
import com.ng.rapetracker.utils.toast
import com.ng.rapetracker.viewmodel.ModelLoginActivity
import com.ng.rapetracker.viewmodel.RapeComplainFormViewModel


class FragmentRegisterOrgSupportType : BaseFragment() {
    lateinit var ADAPTER : AdapterRapeSupportOrgType

    lateinit var binding: FragmentRegisterOrgSupportTypeBinding
    private lateinit var thisContext: Activity


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Get a reference to the binding object and inflate the fragment views.
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_org_support_type, container, false)
        thisContext = requireActivity()


        val application = requireNotNull(activity).application
        binding.lifecycleOwner = this


        val viewModelFactory = ModelLoginActivity.Factory(application)
        val viewModelLoginActivity = ViewModelProvider(this,viewModelFactory).get(ModelLoginActivity::class.java)


        //OR
        ADAPTER = AdapterRapeSupportOrgType(RapeSupportOrgClickListener {
            // Must find the NavController from the Fragment
            this.findNavController().navigate(FragmentRegisterOrgSupportTypeDirections.actionFragmentRegisterOrgSupportTypeToFragmentRegisterOrgDetail(it))
        })
        binding.rapeSupportTypeFormRecycler.apply {
            adapter = ADAPTER
            layoutManager= LinearLayoutManager(requireActivity())
        }
        binding.rapeSupportTypeFormRecycler.adapter = ADAPTER

        viewModelLoginActivity.allRapeSupportType.observe(viewLifecycleOwner, Observer {
            ADAPTER.apply {
                rapeSupportTypes = it.sortedBy { it.id }
            }
        })


        return binding.root
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This callback will only be called when MyFragment is at least Started.

        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    goBack()
                }
            }
//        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

    }
    fun goBack(){
        this@FragmentRegisterOrgSupportType.findNavController().navigate(FragmentRegisterOrgSupportTypeDirections.actionFragmentRegisterOrgSupportTypeToFragmentChooseRegType())
    }

}