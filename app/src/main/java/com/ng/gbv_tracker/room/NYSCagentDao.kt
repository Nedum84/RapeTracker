package com.ng.gbv_tracker.room


import androidx.lifecycle.LiveData
import androidx.room.*
import com.ng.gbv_tracker.model.NYSCagent
import com.ng.gbv_tracker.room.TableNames.Companion.NYSC_AGENT

@Dao
interface NYSCagentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upSert(list: List<NYSCagent>)

    @Query("SELECT * from $NYSC_AGENT WHERE agent_id  = :id")
    suspend fun getById(id: Long): NYSCagent?

    @Query("SELECT * FROM $NYSC_AGENT ORDER BY distance_int ASC")
    fun getAll(): LiveData<List<NYSCagent>>


    @Query("DELETE FROM $NYSC_AGENT")
    fun delete()

}

