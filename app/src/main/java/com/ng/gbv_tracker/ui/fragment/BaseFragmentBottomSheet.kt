package com.ng.gbv_tracker.ui.fragment

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.ng.gbv_tracker.model.User
import com.ng.gbv_tracker.room.DatabaseRoom
import com.ng.gbv_tracker.utils.ClassProgressDialog
import com.ng.gbv_tracker.utils.ClassSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseFragmentBottomSheet : BottomSheetDialogFragment(), CoroutineScope{
    val thisContext: Activity by lazy { requireActivity() }
    val application: Application by lazy { requireNotNull(this.activity).application }
    val db:DatabaseRoom by lazy { DatabaseRoom.getDatabaseInstance(application) }
    val pDialog by lazy { ClassProgressDialog(thisContext) }
    val prefs: ClassSharedPreferences by lazy { ClassSharedPreferences(thisContext) }
    val userDetail: User by lazy { Gson().fromJson(prefs.getCurUserDetail(), User::class.java) }

    private lateinit var job: Job


    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        job = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}