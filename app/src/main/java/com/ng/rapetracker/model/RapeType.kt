package com.ng.rapetracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ng.rapetracker.room.TableNames
import com.ng.rapetracker.room.TableNames.Companion.RAPE_TYPE

@Entity(tableName = RAPE_TYPE)
class RapeType (
    @PrimaryKey
    val  id:Int,
    @ColumnInfo(name = "rape_type")
    val rapeType: String
)