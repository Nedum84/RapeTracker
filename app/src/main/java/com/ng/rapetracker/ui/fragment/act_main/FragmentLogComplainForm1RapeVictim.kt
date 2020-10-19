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
import com.ng.rapetracker.databinding.FragmentLogComplainForm1RapeVictimBinding
import com.ng.rapetracker.model.RapeDetail
import com.ng.rapetracker.model.User
import com.ng.rapetracker.utils.ClassSharedPreferences
import com.ng.rapetracker.viewmodel.GetRapeDetailViewModel
import com.ng.rapetracker.viewmodel.factory.GetRapeDetailViewModelFactory

class FragmentLogComplainForm1RapeVictim : Fragment() {
    lateinit var binding: FragmentLogComplainForm1RapeVictimBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_log_complain_form1_rape_victim, container, false)


        binding.lifecycleOwner = this

        val userDetail = Gson().fromJson(ClassSharedPreferences(requireActivity()).getCurUserDetail(), User::class.java)
        val rapeDetail = RapeDetail(0, false,0,0,0,"","",0,"",0)
        rapeDetail.userName = userDetail.userName

        binding.rapeAgainstYouBtn.setOnClickListener {
            rapeDetail.rapeAgainstYou = true
            this.findNavController().navigate(FragmentLogComplainForm1RapeVictimDirections.actionFragmentLogComplainForm1RapeVictimToFragmentLogComplainForm2TypeOfVictim(rapeDetail))
        }
        binding.againstSomeoneIKnowBtn.setOnClickListener {
            rapeDetail.rapeAgainstYou = false
            this.findNavController().navigate(FragmentLogComplainForm1RapeVictimDirections.actionFragmentLogComplainForm1RapeVictimToFragmentLogComplainForm2TypeOfVictim(rapeDetail))
        }




        return binding.root
    }


}