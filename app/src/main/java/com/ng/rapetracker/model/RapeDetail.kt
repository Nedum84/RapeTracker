package com.ng.rapetracker.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ng.rapetracker.room.TableNames.Companion.RAPE_DETAIL_TABLE
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = RAPE_DETAIL_TABLE)
class RapeDetail (
    @PrimaryKey
    val _id:Int,
    var rapeAgainstYou:Boolean,
    var typeOfVictim: Int,
    var typeOfRape: Int,
    var rapeSupportType: Int,
    var rapeAddress: String,
    var rapeDescription: String,
    var userId:Int,
    var userName:String,
    var userAge:Int
):Parcelable