package com.ng.rapetracker.ui.fragment.act_login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.ng.rapetracker.R
import com.ng.rapetracker.databinding.FragmentPromptToLoginBinding


class FragmentPromptToLogin : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding:FragmentPromptToLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_prompt_to_login, container, false)

        binding.gotoLoginBtn.setOnClickListener {
            this.findNavController().navigate(FragmentPromptToLoginDirections.actionFragmentPromptToLoginToFragmentLogin())
        }
        binding.nyscRegBtn.setOnClickListener {
            this.findNavController().navigate(FragmentPromptToLoginDirections.actionFragmentPromptToLoginToFragmentRegisterNYSCAgent())
        }
        binding.victimRegBtn.setOnClickListener {
            this.findNavController().navigate(FragmentPromptToLoginDirections.actionFragmentPromptToLoginToFragmentRegisterVictim())
        }
        return binding.root
    }

}