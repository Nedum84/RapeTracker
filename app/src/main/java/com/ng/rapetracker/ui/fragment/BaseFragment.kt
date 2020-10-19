package com.ng.rapetracker.ui.fragment

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ng.rapetracker.room.DatabaseRoom
import com.ng.rapetracker.utils.ClassSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext


abstract class BaseFragment : Fragment(), CoroutineScope{
    lateinit var thisContext: Activity
//    lateinit var prefs: ClassSharedPreferences
//    lateinit var myDetails: MyDetails
    val application: Application by lazy { requireNotNull(this.activity).application }
    val db:DatabaseRoom by lazy { DatabaseRoom.getDatabaseInstance(application) }
    val prefs:ClassSharedPreferences by lazy { ClassSharedPreferences(application) }

    private lateinit var job: Job


    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        thisContext = requireActivity()
//        myDetails = prefs.getCurUserDetail()


        job = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}