package com.ng.gbv_tracker.ui.fragment.act_main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.ng.gbv_tracker.R
import com.ng.gbv_tracker.databinding.FragmentProfileBinding
import com.ng.gbv_tracker.databinding.FragmentRapeDetailBinding
import com.ng.gbv_tracker.model.NYSCagent
import com.ng.gbv_tracker.model.Organization
import com.ng.gbv_tracker.model.RapeDetail
import com.ng.gbv_tracker.model.User
import com.ng.gbv_tracker.room.DatabaseRoom
import com.ng.gbv_tracker.utils.ClassAlertDialog
import com.ng.gbv_tracker.utils.ClassSharedPreferences
import com.ng.gbv_tracker.viewmodel.GetRapeDetailViewModel
import com.ng.gbv_tracker.viewmodel.factory.GetRapeDetailViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FragmentProfile : Fragment() {
    lateinit var prefs: ClassSharedPreferences
    lateinit var databaseRoom: DatabaseRoom
    lateinit var binding: FragmentProfileBinding
    lateinit var rapeDetail: RapeDetail

    lateinit var orgDetail:Organization
    lateinit var userDetail:User

    lateinit var nysCagent: NYSCagent


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        prefs = ClassSharedPreferences(requireActivity())

        val application = requireNotNull(this.activity).application
        databaseRoom = DatabaseRoom.getDatabaseInstance(application)


        if (prefs.getAccessLevel() == 1){
            userDetail = Gson().fromJson(prefs.getCurUserDetail(), User::class.java)
            binding.name.text = userDetail.userName
            binding.registeredAs.text = "Victim/Witness"
            binding.emailAddress.text = userDetail.userEmail
            binding.mobileNo.text = userDetail.userMobileNo
            binding.address.text = userDetail.userAddress
            binding.gender.text = if (userDetail.userGender==2)"Male" else "Female"
            binding.stateCodeParent.visibility = View.GONE
        }else if (prefs.getAccessLevel() == 2){
            nysCagent = Gson().fromJson(prefs.getCurNYSCAgent(), NYSCagent::class.java)

            binding.name.text = nysCagent.name
            binding.registeredAs.text = "NYSC Support Agent"
            binding.emailAddress.text = nysCagent.email
            binding.mobileNo.text = nysCagent.mobile_no
            binding.address.text = nysCagent.address
            binding.genderParent.visibility = View.GONE
            binding.stateCode.text = nysCagent.state_code
        }


        binding.reviewbtn.setOnClickListener {
            ClassAlertDialog(requireActivity()).toast("You don't have any review at the moment")
        }
        return binding.root
    }
}