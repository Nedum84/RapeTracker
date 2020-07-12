package com.ng.rapetracker.room



import androidx.lifecycle.LiveData
import androidx.room.*
import com.ng.rapetracker.model.RapeSupportType
import com.ng.rapetracker.model.RapeTypeOfVictim
import com.ng.rapetracker.room.TableNames.Companion.RAPE_TYPE_OF_VICTIM

@Dao
interface RapeTypeOfVictimDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRapeTypeOfVictim(list: List<RapeTypeOfVictim>)

    @Update
    fun updateRapeTypeOfVictim(rapeType: RapeTypeOfVictim)

    @Query("SELECT * from $RAPE_TYPE_OF_VICTIM WHERE id = :id")
    suspend fun getRapeTypeOfVictimById(id: Long): RapeTypeOfVictim?


    @Query("SELECT * FROM $RAPE_TYPE_OF_VICTIM ORDER BY id DESC")
    fun getAllRapeOfVictimType(): LiveData<List<RapeTypeOfVictim>>

    @Query("DELETE FROM $RAPE_TYPE_OF_VICTIM")
    fun clearRapeTypeOfVictim()
}

