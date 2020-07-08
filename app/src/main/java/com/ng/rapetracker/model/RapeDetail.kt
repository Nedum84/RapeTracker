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
    val rapeAgainstYou:Boolean,
    val typeOfVictim: Int,
    val typeOfRape: Int,
    val rapeSupportType: Int,
    val rapeAddress: String,
    val rapeDescription: String,
    val userId:Int,
    val userName:String,
    val userAge:Int
):Parcelable