package com.ng.rapetracker.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ng.rapetracker.room.TableNames.Companion.NYSC_AGENT
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
    val distance: String="0",
    val distance_int: Int=0,
    val mobile_no: String,
    val email: String,
    val cases_attended: Int=0
):Parcelable{

    fun getDistance_() = "$distance from your current location"
}