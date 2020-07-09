package com.ng.rapetracker.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentUris
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.happinesstonic.UrlHolder
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class ClassUtilities() {

    fun calculateNoOfColumns(context:Context, columnWidthDp:Float):Int { // For example columnWidthdp=180
        val displayMetrics = context.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        val noOfColumns = (screenWidthDp / columnWidthDp + 0.5).toInt() // +0.5 for correct rounding to int.
        return noOfColumns
    }
}