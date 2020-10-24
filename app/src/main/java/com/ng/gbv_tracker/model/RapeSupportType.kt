package com.ng.gbv_tracker.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ng.gbv_tracker.room.TableNames.Companion.RAPE_SUPPORT_TYPE
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = RAPE_SUPPORT_TYPE)
class RapeSupportType (
    @PrimaryKey
    val id:Int,
    @ColumnInfo(name = "rape_support_type")
    val rapeSupportType: String
):Parcelable