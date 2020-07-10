package com.ng.rapetracker.ui.fragment.act_login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.ng.rapetracker.R
import com.ng.rapetracker.databinding.FragmentChooseRegTypeBinding
import com.ng.rapetracker.databinding.FragmentPromptToLoginBinding


class FragmentChooseRegType : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentChooseRegTypeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_reg_type, container, false)

        binding.regAsRapeVictimBtn.setOnClickListener {
            this.findNavController().navigate(FragmentChooseRegTypeDirections.actionFragmentChooseRegTypeToFragmentRegisterVictim())
        }
        binding.regAsRapeSupportBtn.setOnClickListener {
            this.findNavController().navigate(FragmentChooseRegTypeDirections.actionFragmentChooseRegTypeToFragmentRegisterOrgSupportType())
        }
        return binding.root
    }

}