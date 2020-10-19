package com.ng.rapetracker.ui.fragment.act_main

import android.app.Dialog
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ng.rapetracker.R
import com.ng.rapetracker.adapter.AdapterNYSCAgent
import com.ng.rapetracker.adapter.NYSCAgentClickListener
import com.ng.rapetracker.databinding.FragmentLogComplainForm6NyscAgentBinding
import com.ng.rapetracker.ui.fragment.BaseFragmentBottomSheet
import com.ng.rapetracker.utils.ClassAlertDialog
import com.ng.rapetracker.viewmodel.GetRapeDetailViewModel
import com.ng.rapetracker.viewmodel.ModelNYSCAgent
import com.ng.rapetracker.viewmodel.factory.GetRapeDetailViewModelFactory


class FragmentLogComplainForm6NYSCAgent : BaseFragmentBottomSheet() {
    lateinit var binding:FragmentLogComplainForm6NyscAgentBinding

    lateinit var modelNYSCAgent: ModelNYSCAgent
    lateinit var ADAPTER: AdapterNYSCAgent





    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        ADAPTER = AdapterNYSCAgent(NYSCAgentClickListener {
            modelNYSCAgent.setItemCategory(it)
            dialog?.dismiss()
            dismiss()
        })

        binding.recyclerNyscAgent.apply {
            adapter = ADAPTER
            layoutManager= LinearLayoutManager(thisContext)
            itemAnimator = DefaultItemAnimator()
            isNestedScrollingEnabled = false
        }


        val vFactory = ModelNYSCAgent.Factory(application)
        modelNYSCAgent =  requireActivity().run{
            ViewModelProvider(this, vFactory).get(ModelNYSCAgent::class.java)
        }



        modelNYSCAgent.nyscAgents.observe(viewLifecycleOwner, Observer {
            it?.let{
                ADAPTER.list = it
            }
        })
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_log_complain_form6_nysc_agent, container, false)




        return binding.root
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return BottomSheetDialog(requireContext(), theme).apply {
//            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
////            behavior.state = BottomSheetBehavior.STATE_EXPANDED
////            behavior.peekHeight = PEEK_HEIGHT_AUTO
////            behavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels
//            behavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels-200
//        }
//    }

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
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
        return bottomSheetDialog
    }

}

