package com.ng.rapetracker.room


import androidx.lifecycle.LiveData
import androidx.room.*
import com.ng.rapetracker.model.RapeType
import com.ng.rapetracker.room.TableNames.Companion.RAPE_TYPE

@Dao
interface RapeTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRapeType(vararg rapeType: RapeType)

    @Update
    fun updateRapeType(rapeType: RapeType)

    @Query("SELECT * from $RAPE_TYPE WHERE _id = :_id")
    fun getRapeTypeById(_id: Long): RapeType?


    @Query("SELECT * FROM $RAPE_TYPE ORDER BY _id DESC")
    fun getAllRapeType(): LiveData<List<RapeType>>

    @Query("DELETE FROM $RAPE_TYPE")
    fun clearRapeType()
}

