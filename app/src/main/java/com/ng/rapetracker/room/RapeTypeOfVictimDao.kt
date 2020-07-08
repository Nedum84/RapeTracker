package com.ng.rapetracker.room



import androidx.lifecycle.LiveData
import androidx.room.*
import com.ng.rapetracker.model.RapeSupportType
import com.ng.rapetracker.room.TableNames.Companion.RAPE_TYPE_OF_VICTIM

@Dao
interface RapeTypeOfVictimDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRapeTypeOfVictim(list: List<RapeSupportType>)

    @Update
    fun updateRapeTypeOfVictim(rapeType: RapeSupportType)

    @Query("SELECT * from $RAPE_TYPE_OF_VICTIM WHERE _id = :_id")
    fun getRapeTypeOfVictimById(_id: Long): RapeSupportType?


    @Query("SELECT * FROM $RAPE_TYPE_OF_VICTIM ORDER BY _id DESC")
    fun getAllRapeOfVictimType(): LiveData<List<RapeSupportType>>

    @Query("DELETE FROM $RAPE_TYPE_OF_VICTIM")
    fun clearRapeTypeOfVictim()
}

