package com.ng.rapetracker.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ng.rapetracker.room.TableNames.Companion.RAPE_SUPPORT_TYPE
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = RAPE_SUPPORT_TYPE)
class RapeSupportType (
    @PrimaryKey
    val _id:Int,
    @ColumnInfo(name = "rape_support_type")
    val rapeSupportType: String
):Parcelable