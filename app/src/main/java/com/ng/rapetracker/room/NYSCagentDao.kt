package com.ng.rapetracker.room


import androidx.lifecycle.LiveData
import androidx.room.*
import com.ng.rapetracker.model.NYSCagent
import com.ng.rapetracker.room.TableNames.Companion.NYSC_AGENT

@Dao
interface NYSCagentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upSert(list: List<NYSCagent>)

    @Query("SELECT * from $NYSC_AGENT WHERE agent_id  = :id")
    suspend fun getById(id: Long): NYSCagent?

    @Query("SELECT * FROM $NYSC_AGENT")
    fun getAll(): LiveData<List<NYSCagent>>


    @Query("DELETE FROM $NYSC_AGENT")
    fun delete()

}

