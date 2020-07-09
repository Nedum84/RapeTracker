package com.ng.rapetracker.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ng.rapetracker.model.State

@Dao
interface StateDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upSertState(vararg state: State)


    @Query("SELECT * from ${TableNames.STATE} WHERE id = :id")
    fun getStateById(id: Long): State?


    @Query("SELECT * FROM ${TableNames.STATE} ORDER BY id DESC")
    fun getAllState(): List<State>
}