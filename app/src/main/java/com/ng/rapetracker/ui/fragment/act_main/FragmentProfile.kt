package com.ng.rapetracker.ui.fragment.act_main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.ng.rapetracker.R
import com.ng.rapetracker.databinding.FragmentProfileBinding
import com.ng.rapetracker.databinding.FragmentRapeDetailBinding
import com.ng.rapetracker.model.Organization
import com.ng.rapetracker.model.RapeDetail
import com.ng.rapetracker.model.User
import com.ng.rapetracker.room.DatabaseRoom
import com.ng.rapetracker.utils.ClassSharedPreferences
import com.ng.rapetracker.viewmodel.GetRapeDetailViewModel
import com.ng.rapetracker.viewmodel.factory.GetRapeDetailViewModelFactory
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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        prefs = ClassSharedPreferences(requireActivity())

        val application = requireNotNull(this.activity).application
        databaseRoom = DatabaseRoom.getDatabaseInstance(application)


        if (prefs.getAccessLevel() == 1){
            userDetail = Gson().fromJson(prefs.getCurUserDetail(), User::class.java)
            binding.typeOfUser.text = "Registered as a Rape Victim/Witness"
        }else if (prefs.getAccessLevel() == 2){
            orgDetail = Gson().fromJson(prefs.getCurOrgDetail(), Organization::class.java)



            CoroutineScope((Dispatchers.Default)).launch {
                val org = databaseRoom.rapeSupportTypeDao.getRapeSupportById(orgDetail.orgType)
                binding.typeOfUser.text = "Registered as Support Organization (\"${org!!.rapeSupportType}\")"
            }
        }

        return binding.root
    }
}