package com.ng.gbv_tracker.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ng.gbv_tracker.model.State

@Dao
interface StateDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upSertState(list: List<State>)


    @Query("SELECT * from ${TableNames.STATE} WHERE id = :id")
    fun getStateById(id: Long): State?

    @Query("SELECT * from ${TableNames.STATE} WHERE country_id = :id ")
    suspend fun getStateByCountryId(id: Int): List<State?>


    @Query("SELECT * FROM ${TableNames.STATE} ORDER BY id DESC")
    suspend fun getAllState(): List<State>
}