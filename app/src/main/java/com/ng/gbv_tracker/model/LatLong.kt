package com.ng.gbv_tracker.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
class LatLong(
    var lat: Double,
    var long:Double,
    var address:String = ""
):Parcelable