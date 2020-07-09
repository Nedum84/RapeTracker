package com.ng.rapetracker.ui.fragment.act_login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.ng.rapetracker.R


class FragmentPromptToLogin : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        this.findNavController().navigate(FragmentP)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_prompt_to_login, container, false)
    }

}