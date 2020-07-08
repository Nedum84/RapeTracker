package com.ng.rapetracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ng.rapetracker.room.TableNames

@Entity(tableName = TableNames.STATE)
class State (
    @PrimaryKey
    val  _id:Int,
    val name: String,
    @ColumnInfo(name = "country_id")
    val countryId:Int
)
