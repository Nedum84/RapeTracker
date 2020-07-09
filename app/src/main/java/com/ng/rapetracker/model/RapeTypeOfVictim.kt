package com.ng.rapetracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ng.rapetracker.room.TableNames.Companion.RAPE_TYPE_OF_VICTIM

@Entity(tableName = RAPE_TYPE_OF_VICTIM)
class RapeTypeOfVictim (
    @PrimaryKey
    val id : Int,
    @ColumnInfo(name = "type_of_victim")//optional, to indicate the name of the table col in the database. default(val param name) is used when not specified
    val typeOfVictim:String
)