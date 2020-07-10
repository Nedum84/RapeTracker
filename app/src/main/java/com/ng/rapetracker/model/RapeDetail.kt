package com.ng.rapetracker.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ng.rapetracker.room.TableNames.Companion.RAPE_DETAIL_TABLE
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = RAPE_DETAIL_TABLE)
class RapeDetail (
    @PrimaryKey
    val id:Int,
    @ColumnInfo(name = "rape_against_you")
    var rapeAgainstYou:Boolean,
    @ColumnInfo(name = "type_of_victim")
    var typeOfVictim: Int,
    @ColumnInfo(name = "type_of_rape")
    var typeOfRape: Int,
    @ColumnInfo(name = "rape_support_type")
    var rapeSupportType: Int,
    @ColumnInfo(name = "rape_address")
    var rapeAddress: String,
    @ColumnInfo(name = "rape_description")
    var rapeDescription: String,
    @ColumnInfo(name = "user_id")
    var userId:Int,
    @ColumnInfo(name = "user_name")
    var userName:String,
    @ColumnInfo(name = "user_age")
    var userAge:Int,
    @ColumnInfo(name = "date_added")
    var dateAdded:Long = System.currentTimeMillis()
):Parcelable
