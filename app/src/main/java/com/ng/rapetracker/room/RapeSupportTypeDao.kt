package com.ng.rapetracker.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ng.rapetracker.model.RapeDetail
import com.ng.rapetracker.model.RapeSupportType
import com.ng.rapetracker.room.TableNames.Companion.RAPE_SUPPORT_TYPE


@Dao
interface RapeSupportTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRapeSupport(list: List<RapeSupportType>)

    @Update
    fun updateRapeSupport(rapeSupportType: RapeSupportType)


    @Query("SELECT * from $RAPE_SUPPORT_TYPE WHERE id = :id")
    suspend fun getRapeSupportById(id: Int): RapeSupportType?


    @Query("SELECT * FROM $RAPE_SUPPORT_TYPE ORDER BY id DESC")
    fun getAllRapeSupport(): LiveData<List<RapeSupportType>>

    @Query("DELETE FROM $RAPE_SUPPORT_TYPE")
    fun clearRapeSupport()

}

