package com.ng.rapetracker.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ng.rapetracker.model.SARCsSupport
import com.ng.rapetracker.room.TableNames.Companion.SARC_SUPPORT


@Dao
interface SARCsSupportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upSert(list: List<SARCsSupport>)


    @Query("SELECT * from $SARC_SUPPORT WHERE sarcs_id = :id")
    suspend fun getById(id: Int): SARCsSupport?


    @Query("SELECT * FROM $SARC_SUPPORT ORDER BY state DESC")
    fun getAll(): LiveData<List<SARCsSupport>>

    @Query("DELETE FROM $SARC_SUPPORT")
    fun clearRapeSupport()

}

