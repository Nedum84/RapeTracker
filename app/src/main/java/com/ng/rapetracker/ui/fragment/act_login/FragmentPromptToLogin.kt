package com.ng.rapetracker.ui.fragment.act_login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.ng.rapetracker.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentPromptToLogin.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentPromptToLogin : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        this.findNavController().navigate(FragmentP)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_prompt_to_login, container, false)
    }

}