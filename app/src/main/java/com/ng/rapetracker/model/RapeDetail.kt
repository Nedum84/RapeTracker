package com.ng.rapetracker.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ng.rapetracker.room.TableNames.Companion.RAPE_DETAIL_TABLE
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize


//@JsonClass(generateAdapter = true)
@Parcelize
@Entity(tableName = RAPE_DETAIL_TABLE)
class RapeDetail (
    @PrimaryKey
    val id:Int,
    @Json(name = "rape_against_you")//for moshi
    @ColumnInfo(name = "rape_against_you")
    var rapeAgainstYou:Boolean,

    @Json(name = "type_of_victim")//for moshi
    @ColumnInfo(name = "type_of_victim")
    var typeOfVictim: Int,

    @Json(name = "type_of_rape")//for moshi
    @ColumnInfo(name = "type_of_rape")
    var typeOfRape: Int,

    @Json(name = "rape_support_type")//for moshi
    @ColumnInfo(name = "rape_support_type")
    var rapeSupportType: Int,

    @Json(name = "rape_address")//for moshi
    @ColumnInfo(name = "rape_address")
    var rapeAddress: String,

    @Json(name = "rape_description")//for moshi
    @ColumnInfo(name = "rape_description")
    var rapeDescription: String,

    @Json(name = "user_id")//for moshi
    @ColumnInfo(name = "user_id")
    var userId:Int,

    @Json(name = "user_name")//for moshi
    @ColumnInfo(name = "user_name")
    var userName:String,

    @Json(name = "user_age")//for moshi
    @ColumnInfo(name = "user_age")
    var userAge:Int,

    @Json(name = "date_added")//for moshi
    @ColumnInfo(name = "date_added")
    var dateAdded:Long = System.currentTimeMillis(),

    @Json(name = "nysc_agent_id")//for moshi
    @ColumnInfo(name = "nysc_agent_id")
    var nyscAgentId:Int=0,

    @Json(name = "is_case_resolved")//for moshi
    @ColumnInfo(name = "is_case_resolved")
    var isAaseResolved:Int = 0
):Parcelable
