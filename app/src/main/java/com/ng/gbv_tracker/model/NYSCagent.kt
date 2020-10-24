package com.ng.gbv_tracker.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ng.gbv_tracker.room.TableNames.Companion.NYSC_AGENT
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = NYSC_AGENT)
class NYSCagent (
    @PrimaryKey
    val agent_id:Int,
    val name: String,
    val state_code: String,
    val address: String,
    val latitude: String,
    val longitude: String,
    var distance: String="0KM",
    var distance_int: Double=0.0,
    val mobile_no: String,
    val email: String,
    val cases_attended: Int=0
):Parcelable{

    fun getDistance_() = "$distance from your current location"
}