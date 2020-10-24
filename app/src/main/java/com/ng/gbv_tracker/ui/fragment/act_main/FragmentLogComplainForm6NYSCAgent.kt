package com.ng.gbv_tracker.ui.fragment.act_main

import android.app.Dialog
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ng.gbv_tracker.R
import com.ng.gbv_tracker.adapter.AdapterNYSCAgent
import com.ng.gbv_tracker.adapter.NYSCAgentClickListener
import com.ng.gbv_tracker.databinding.FragmentLogComplainForm6NyscAgentBinding
import com.ng.gbv_tracker.model.LatLong
import com.ng.gbv_tracker.model.NYSCagent
import com.ng.gbv_tracker.ui.fragment.BaseFragmentBottomSheet
import com.ng.gbv_tracker.utils.ClassAlertDialog
import com.ng.gbv_tracker.viewmodel.GetRapeDetailViewModel
import com.ng.gbv_tracker.viewmodel.ModelNYSCAgent
import com.ng.gbv_tracker.viewmodel.factory.GetRapeDetailViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat


class FragmentLogComplainForm6NYSCAgent : BaseFragmentBottomSheet() {
    lateinit var binding:FragmentLogComplainForm6NyscAgentBinding

    lateinit var modelNYSCAgent: ModelNYSCAgent
    lateinit var ADAPTER: AdapterNYSCAgent

    var dbUpdated = false





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_log_complain_form6_nysc_agent, container, false)




        return binding.root
    }

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
                if(!dbUpdated)computeDistance(it)
                ADAPTER.list = it


                if(it.isEmpty()){
                    dismiss()
                    ClassAlertDialog(thisContext).alertMessage("No NYSC agent registered at this moment. Check back Later!", "NO AGENT!")
                }
            }
        })
    }

    val latLongA:LatLong by lazy { LatLong(userDetail.latitude.toDouble(), userDetail.longitude.toDouble(), userDetail.userAddress) }
    private fun computeDistance(agents: List<NYSCagent>) {
        val newList = mutableListOf<NYSCagent>()

        agents.forEach {
            try {
                val latLongB = LatLong(it.latitude.toDouble(), it.longitude.toDouble())
                val distance = calculationByDistance(latLongB)
                it.distance_int = distance
                it.distance = String.format("%.1f",distance)+ "Km"
                newList.add(it)
            } catch (e: Exception) {e.printStackTrace()}
        }

        CoroutineScope(Dispatchers.IO).launch {
            db.nYSCagentDao.upSert(newList)
        }
        dbUpdated = true
    }
    fun calculationByDistance(latLongB:LatLong): Double {
        val Radius = 6371 // radius of earth in Km
        val lat1 = latLongA.lat
        val lat2 = latLongB.lat
        val lon1 = latLongA.long
        val lon2 = latLongB.long
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = (Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + (Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2)))
        val c = 2 * Math.asin(Math.sqrt(a))
        val valueResult = Radius * c
        val km = valueResult / 1
        val newFormat = DecimalFormat("####")
        val kmInDec: Int = Integer.valueOf(newFormat.format(km))
        val meter = valueResult % 1000
        val meterInDec: Int = Integer.valueOf(newFormat.format(meter))

        return Radius * c
    }




    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =  bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_COLLAPSED
                behaviour.peekHeight = Resources.getSystem().displayMetrics.heightPixels-200
            }
        }
        return dialog
    }
    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

}

