package com.ng.gbv_tracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ng.gbv_tracker.room.TableNames.Companion.RAPE_TYPE

@Entity(tableName = RAPE_TYPE)
class RapeType (
    @PrimaryKey
    val  id:Int,
    @ColumnInfo(name = "rape_type")
    val rapeType: String,
    @ColumnInfo(name = "rape_description")
    val rapeDescription:String
)