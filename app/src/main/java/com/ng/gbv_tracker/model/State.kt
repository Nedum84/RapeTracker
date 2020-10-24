package com.ng.gbv_tracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ng.gbv_tracker.room.TableNames

@Entity(tableName = TableNames.STATE)
class State (
    @PrimaryKey
    val  id:Int,
    val name: String,
    @ColumnInfo(name = "country_id")
    val countryId:Int
)
