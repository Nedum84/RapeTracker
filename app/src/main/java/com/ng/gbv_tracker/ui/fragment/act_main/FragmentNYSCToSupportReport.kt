package com.ng.gbv_tracker.ui.fragment.act_main

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.os.Parcelable
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
import com.google.gson.Gson
import com.ng.gbv_tracker.R
import com.ng.gbv_tracker.adapter.AdapterSARCsSupport
import com.ng.gbv_tracker.adapter.SARCsSupportClickListener
import com.ng.gbv_tracker.databinding.FragmentNyscToSupportReportBinding
import com.ng.gbv_tracker.model.*
import com.ng.gbv_tracker.network.RetrofitConstant
import com.ng.gbv_tracker.network.ServerResponse
import com.ng.gbv_tracker.ui.fragment.BaseFragmentBottomSheet
import com.ng.gbv_tracker.utils.ClassAlertDialog
import com.ng.gbv_tracker.utils.ClassSharedPreferences
import com.ng.gbv_tracker.utils.toast
import com.ng.gbv_tracker.viewmodel.ModelSarcsSupport
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


private const val RAPE_DETAIL = "rape_detail"

class FragmentNYSCToSupportReport : BaseFragmentBottomSheet() {
    private var rapeDetail: RapeDetail? = null
    lateinit var binding:FragmentNyscToSupportReportBinding
    lateinit var modelSarcsSupport: ModelSarcsSupport

    lateinit var ADAPTER: AdapterSARCsSupport




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        arguments?.let {
            rapeDetail = it.getParcelable(RAPE_DETAIL)
//            if(rapeDetail!=null){
//                ClassAlertDialog(thisContext).alertMessage("Click on any SARCs to assign this incidence to.","Assign to SARCs")
//            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        ADAPTER = AdapterSARCsSupport(object :SARCsSupportClickListener{
            override fun onClick(item: SARCsSupport) {
                if(rapeDetail!=null){
                    AlertDialog.Builder(thisContext)
                        .setTitle("Confirm!")
                        .setMessage("Assign this case to ${item.name} (ADDRESS: ${item.address})?")
                        .setPositiveButton("OK"
                        ) { _, id ->
                            assignToSarcs(item)
                        }.setNegativeButton("CANCEL"
                        ) { dialog, id ->
                        }.show()
                }else{
                    requireActivity().run {
                        FragmentAboutSarcs.newInstance(item).show(this.supportFragmentManager, tag)
                    }
                }
            }

            override fun onMoreClick(item: SARCsSupport) {
                requireActivity().run {
                    FragmentAboutSarcs.newInstance(item).show(this.supportFragmentManager, tag)
                }
            }
        })

        binding.recyclerSupport.apply {
            adapter = ADAPTER
            layoutManager= LinearLayoutManager(thisContext)
            itemAnimator = DefaultItemAnimator()
            isNestedScrollingEnabled = false
        }


        modelSarcsSupport =  requireActivity().run{
            ViewModelProvider(this, ModelSarcsSupport.Factory(application)).get(ModelSarcsSupport::class.java)
        }

        modelSarcsSupport.sarcsSupports.observe(viewLifecycleOwner, Observer {
            it?.let{
                ADAPTER.list = it
            }
        })
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nysc_to_support_report, container, false)



        if (ClassSharedPreferences(requireActivity()).getAccessLevel()==2&&rapeDetail!=null){
            binding.clickToAssign.visibility = View.VISIBLE
        }else{
            binding.clickToAssign.visibility = View.GONE
        }

        return binding.root
    }


    private fun assignToSarcs(item: SARCsSupport, assigned_level: Int = 1) {
        pDialog.createDialog()
        //Sending the request
        val nysCagent = Gson().fromJson(prefs.getCurNYSCAgent(), NYSCagent::class.java)


        RetrofitConstant.RetrofitConstantGET.create(AssignToSarcsService::class.java).assign(
            nysc_agent_id = nysCagent.agent_id,
            rape_detail_id = rapeDetail!!.id,
            sarcs_id = item.sarcs_id,
            victim_id = rapeDetail!!.userId
        ).enqueue(object : Callback<ServerResponse> {
            override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                pDialog.dismissDialog()
                thisContext.toast("NETWORK ERROR")
                t.printStackTrace()
            }

            override fun onResponse(call: Call<ServerResponse>, response: Response<ServerResponse>) {
                pDialog.dismissDialog()
                if (!response.isSuccessful){
                    thisContext.toast("NETWORK ERROR")
                }else{
                    val resp = response.body()
                    if (!resp!!.success!!){
                        thisContext.toast(resp.respMessage!!)
                    }else{
                        ClassAlertDialog(thisContext).alertMessage(
                            "${rapeDetail?.userName!!.split("/")[0]}'s case has been assigned successfully to ${item.name} (ADDRESS: ${item.address})", "CASE ASSIGNED!"
                        )

                        try {//add to database
//                            val moshi: Moshi = Moshi.Builder().build()
//                            val type = Types.newParameterizedType(MutableList::class.java, RapeDetail::class.java)
//                            val adapter: JsonAdapter<List<RapeDetail>> = moshi.adapter(type)
//                            val rDetail: List<RapeDetail> = adapter.fromJson(resp.otherDetail!!)!!

                            rapeDetail!!.isCaseResolved = assigned_level

                            CoroutineScope(Dispatchers.IO).launch {
                                db.rapeDetailDao.insertRapeDetail(mutableListOf(rapeDetail!!))
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }finally {
                            dialog?.dismiss()
                        }
                    }
                }
            }

        })
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
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
        return bottomSheetDialog
    }


    companion object {


        @JvmStatic
        fun newInstance(param1: Parcelable) =
            FragmentNYSCToSupportReport().apply {
                arguments = Bundle().apply {
                    putParcelable(RAPE_DETAIL, param1)
                }
            }
    }
}



interface AssignToSarcsService{

    @Multipart
    @POST("assign_to_sarc.php")
    fun assign(
        @Part("nysc_agent_id") nysc_agent_id:Int,
        @Part("sarcs_id") sarcs_id: Int,
        @Part("victim_id") victim_id: Int,
        @Part("rape_detail_id") rape_detail_id:Int,
        @Part("assigned_level") assigned_level:Int=1
    ): Call<ServerResponse>
}