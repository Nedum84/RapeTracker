package com.ng.gbv_tracker.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ng.gbv_tracker.room.TableNames.Companion.SARC_SUPPORT
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = SARC_SUPPORT)
class SARCsSupport (
    @PrimaryKey
    val sarcs_id:Int,
    val name: String,
    val state: String,
    val address: String,
    val mobile_nos: String,
    val emails: String,
    val social_fb: String,
    val social_twitter: String,
    val manager_name: String,
    val manager_phone: String,
    val manager_email: String
):Parcelable