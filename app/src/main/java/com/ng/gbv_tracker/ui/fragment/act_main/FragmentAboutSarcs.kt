package com.ng.gbv_tracker.ui.fragment.act_main

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ng.gbv_tracker.R
import com.ng.gbv_tracker.databinding.FragmentAboutSarcsBinding
import com.ng.gbv_tracker.model.SARCsSupport
import com.ng.gbv_tracker.ui.fragment.BaseFragmentBottomSheet


private const val SARC = "sarc_current"


class FragmentAboutSarcs : BaseFragmentBottomSheet() {
    private var sarc: SARCsSupport? = null
    lateinit var binding:FragmentAboutSarcsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        arguments?.let {
            sarc = it.getParcelable(SARC)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (sarc==null){
            dismiss()
        }

        binding.centerName.text = sarc!!.name
        binding.state.text = sarc!!.state+" State"
        binding.address.text = sarc!!.address
        binding.mobileNo.text = sarc!!.mobile_nos.replace(" ", "").replace(",","\n")
        binding.emailAddress.text = sarc!!.emails.replace(" ", "").replace(",","\n")
        binding.twitter.text = sarc!!.social_twitter
        binding.facebook.text = sarc!!.social_fb


        binding.managerName.text = sarc!!.manager_name
        binding.managerEmailAddress.text = sarc!!.manager_email.replace(" ", "").replace(",","\n")
        binding.managerMobileNo.text = sarc!!.manager_phone.replace(" ", "").replace(",","\n")

        if (sarc!!.emails.isEmpty()){
            binding.emailAddressParent.visibility = View.GONE
        }

        if (sarc!!.social_fb.isEmpty()){
            binding.facebookParent.visibility = View.GONE
        }

        if (sarc!!.social_twitter.isEmpty()){
            binding.twitterParent.visibility = View.GONE
        }

        if (sarc!!.manager_email.isEmpty()){
            binding.managerEmailParent.visibility = View.GONE
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentAboutSarcsBinding.inflate(inflater)


        return binding.root
    }




    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog =
            super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { dialog: DialogInterface ->
            val dialogc = dialog as BottomSheetDialog
            // When using AndroidX the resource can be found at com.google.android.material.R.id.design_bottom_sheet
            val bottomSheet: FrameLayout? =
                dialogc.findViewById(com.google.android.material.R.id.design_bottom_sheet)
            val bottomSheetBehavior: BottomSheetBehavior<*> =
                BottomSheetBehavior.from(bottomSheet!!)
            bottomSheetBehavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels-200
//            bottomSheetBehavior.peekHeight = PEEK_HEIGHT_AUTO
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }
        return bottomSheetDialog
    }



    companion object {

        @JvmStatic
        fun newInstance(param1: Parcelable) =
            FragmentAboutSarcs().apply {
                arguments = Bundle().apply {
                    putParcelable(SARC, param1)
                }
            }
    }
}