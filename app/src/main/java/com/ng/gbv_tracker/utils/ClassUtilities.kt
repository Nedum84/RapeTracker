package com.ng.gbv_tracker.utils

import android.content.Context


class ClassUtilities() {

    fun calculateNoOfColumns(context:Context, columnWidthDp:Float):Int { // For example columnWidthdp=180
        val displayMetrics = context.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        val noOfColumns = (screenWidthDp / columnWidthDp + 0.5).toInt() // +0.5 for correct rounding to int.
        return noOfColumns
    }
}