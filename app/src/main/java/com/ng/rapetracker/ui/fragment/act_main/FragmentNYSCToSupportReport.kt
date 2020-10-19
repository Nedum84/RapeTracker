package com.ng.rapetracker.ui.fragment.act_main

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
import com.ng.rapetracker.R
import com.ng.rapetracker.adapter.AdapterSARCsSupport
import com.ng.rapetracker.adapter.SARCsSupportClickListener
import com.ng.rapetracker.databinding.FragmentNyscToSupportReportBinding
import com.ng.rapetracker.model.*
import com.ng.rapetracker.network.RetrofitConstant
import com.ng.rapetracker.network.ServerResponse
import com.ng.rapetracker.ui.fragment.BaseFragmentBottomSheet
import com.ng.rapetracker.utils.ClassAlertDialog
import com.ng.rapetracker.utils.ClassProgressDialog
import com.ng.rapetracker.utils.ClassUtilities
import com.ng.rapetracker.utils.toast
import com.ng.rapetracker.viewmodel.GetRapeDetailViewModel
import com.ng.rapetracker.viewmodel.ModelNYSCAgent
import com.ng.rapetracker.viewmodel.ModelSarcsSupport
import com.ng.rapetracker.viewmodel.factory.GetRapeDetailViewModelFactory
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
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        ADAPTER = AdapterSARCsSupport(object :SARCsSupportClickListener{
            override fun onClick(item: SARCsSupport) {
                AlertDialog.Builder(thisContext)
                    .setTitle("Confirm!")
                    .setMessage("Assign this case to ${item.name} (ADDRESS: ${item.address})?")
                    .setPositiveButton("OK"
                    ) { _, id ->
                        assignToSarcs(item)
                    }.setNegativeButton("CANCEL"
                    ) { dialog, id ->
                    }.show()
            }

            override fun onMoreClick(item: SARCsSupport) {

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




        return binding.root
    }


    private fun assignToSarcs(item: SARCsSupport) {
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
                        thisContext.toast("${rapeDetail?.userName}'s has assigned successfully to ${item.name}(ADDRESS: ${item.address})")

                        try {//add to database
                            val moshi: Moshi = Moshi.Builder().build()
//                                    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                            val type = Types.newParameterizedType(MutableList::class.java, RapeDetail::class.java)
                            val adapter: JsonAdapter<List<RapeDetail>> = moshi.adapter(type)
                            val rDetail: List<RapeDetail> = adapter.fromJson(resp.otherDetail!!)!!
                            CoroutineScope(Dispatchers.IO).launch {
                                db.rapeDetailDao.insertRapeDetail(rDetail)
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




//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return BottomSheetDialog(requireContext(), theme).apply {
//            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
//            behavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels-200
//
//
////            behavior.state = BottomSheetBehavior.STATE_EXPANDED
////            behavior.peekHeight = PEEK_HEIGHT_AUTO
////            behavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels
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


    companion object {


        @JvmStatic
        fun newInstance(param1: Parcelable, param2: String) =
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
        @Part("rape_detail_id") rape_detail_id:Int
    ): Call<ServerResponse>
}