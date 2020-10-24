package com.ng.gbv_tracker.ui.fragment.act_login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ng.gbv_tracker.R
import com.ng.gbv_tracker.databinding.FragmentPromptToLoginBinding
import kotlinx.android.synthetic.main.dialog_choose_reg_type.view.*


class FragmentPromptToLogin : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding:FragmentPromptToLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_prompt_to_login, container, false)

        binding.gotoLoginBtn.setOnClickListener {
            this.findNavController().navigate(FragmentPromptToLoginDirections.actionFragmentPromptToLoginToFragmentLogin())
        }

        binding.regBtn.setOnClickListener {
            chooseRegTypeDialog()
        }
        binding.callHelpLine.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:"+ "08136845243")

            startActivity(callIntent)
        }
        return binding.root
    }

    private fun chooseRegTypeDialog() {

//        val bottomSheetDialogView: View = layoutInflater.inflate(R.layout.bottomsheet_ebook_upload_type, null)
        val bottomSheetDialogView: View =LayoutInflater.from(context).inflate(R.layout.dialog_choose_reg_type, null)
        val bSheetDialog = BottomSheetDialog(requireContext())
        bSheetDialog.setContentView(bottomSheetDialogView)
        bSheetDialog.show()

        bottomSheetDialogView.nyscRegBtn.setOnClickListener {
            bSheetDialog.dismiss()
            this.findNavController().navigate(FragmentPromptToLoginDirections.actionFragmentPromptToLoginToFragmentRegisterNYSCAgent())
        }
        bottomSheetDialogView.victimRegBtn.setOnClickListener {
            bSheetDialog.dismiss()
            this.findNavController().navigate(FragmentPromptToLoginDirections.actionFragmentPromptToLoginToFragmentRegisterVictim())
        }

    }

}