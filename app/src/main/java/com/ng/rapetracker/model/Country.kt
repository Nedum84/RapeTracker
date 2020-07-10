package com.ng.rapetracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ng.rapetracker.room.TableNames



@Entity(tableName = TableNames.COUNTRY)
class Country (
    @PrimaryKey
    val  id:Int,
    val iso: String,
    val name: String,
    val nicename: String,
    val iso3: String,
    val numcode: String,
    val phonecode: String
)