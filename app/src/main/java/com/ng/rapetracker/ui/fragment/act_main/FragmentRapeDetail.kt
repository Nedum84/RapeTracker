package com.ng.rapetracker.ui.fragment.act_main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.ng.rapetracker.R
import com.ng.rapetracker.adapter.AdapterRapeDetail
import com.ng.rapetracker.adapter.RapeDetailClickListener
import com.ng.rapetracker.databinding.FragmentRapeDetailBinding
import com.ng.rapetracker.model.Organization
import com.ng.rapetracker.model.RapeDetail
import com.ng.rapetracker.model.User
import com.ng.rapetracker.room.DatabaseRoom
import com.ng.rapetracker.ui.fragment.BaseFragment
import com.ng.rapetracker.utils.ClassSharedPreferences
import com.ng.rapetracker.viewmodel.GetRapeDetailViewModel
import com.ng.rapetracker.viewmodel.factory.GetRapeDetailViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentRapeDetail : BaseFragment() {
    lateinit var binding:FragmentRapeDetailBinding
    lateinit var rapeDetail: RapeDetail

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rape_detail, container, false)

        val application = requireNotNull(this.activity).application
        rapeDetail = arguments.let { FragmentRapeDetailArgs.fromBundle(it!!).selectedRapeDetail}

        val adapterScope = CoroutineScope(Dispatchers.Default)
        val databaseRoom = DatabaseRoom.getDatabaseInstance(application)

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

        return binding.root
    }
}