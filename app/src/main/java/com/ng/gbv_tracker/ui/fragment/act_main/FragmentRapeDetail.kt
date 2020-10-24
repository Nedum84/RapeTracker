package com.ng.gbv_tracker.ui.fragment.act_main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.ng.gbv_tracker.R
import com.ng.gbv_tracker.adapter.AdapterRapeDetail
import com.ng.gbv_tracker.adapter.RapeDetailClickListener
import com.ng.gbv_tracker.databinding.FragmentRapeDetailBinding
import com.ng.gbv_tracker.model.Organization
import com.ng.gbv_tracker.model.RapeDetail
import com.ng.gbv_tracker.model.User
import com.ng.gbv_tracker.room.DatabaseRoom
import com.ng.gbv_tracker.ui.fragment.BaseFragment
import com.ng.gbv_tracker.utils.ClassSharedPreferences
import com.ng.gbv_tracker.viewmodel.GetRapeDetailViewModel
import com.ng.gbv_tracker.viewmodel.factory.GetRapeDetailViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentRapeDetail : BaseFragment() {
    lateinit var binding:FragmentRapeDetailBinding
    lateinit var rapeDetail: RapeDetail
    val databaseRoom by lazy { DatabaseRoom.getDatabaseInstance(application) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rape_detail, container, false)

        val application = requireNotNull(this.activity).application
        rapeDetail = arguments.let { FragmentRapeDetailArgs.fromBundle(it!!).selectedRapeDetail}


        if (ClassSharedPreferences(requireActivity()).getAccessLevel()==2&&rapeDetail.isCaseResolved==0){
            binding.assignToSarcs.visibility = View.VISIBLE
        }else{
            binding.assignToSarcs.visibility = View.GONE
        }

        binding.assignToSarcs.setOnClickListener {
            requireActivity().run {
                FragmentNYSCToSupportReport.newInstance(rapeDetail).show(this.supportFragmentManager, tag)
            }
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        databaseRoom.rapeDetailDao.getById(rapeDetail.id.toLong()).observe(viewLifecycleOwner, Observer {
            it?.let {
                rapeDetail = it
                loadDetails()
            }
        })
    }
    fun loadDetails(){

        launch {
            binding.rapeReporter.text = rapeDetail.userName

            val typeOfVictim = databaseRoom.rapeTypeOfVictimDao.getRapeTypeOfVictimById(rapeDetail.typeOfVictim.toLong())
            binding.rapeVictim.text = typeOfVictim!!.typeOfVictim


            val rapeType = databaseRoom.rapeTypeDao.getRapeTypeById(rapeDetail.typeOfRape.toLong())
            binding.typeOfRape.text = rapeType!!.rapeType


            val rapeSupportType = databaseRoom.rapeSupportTypeDao.getRapeSupportById(rapeDetail.rapeSupportType)
            binding.typeOfSupport.text = rapeSupportType!!.rapeSupportType


            binding.addressOfOccurrence.text = rapeDetail.rapeAddress
            binding.rapeDesc.text = rapeDetail.rapeDescription

        }
    }
}