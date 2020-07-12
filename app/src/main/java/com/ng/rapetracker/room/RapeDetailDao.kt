package com.ng.rapetracker.room


import androidx.lifecycle.LiveData
import androidx.room.*
import com.ng.rapetracker.model.RapeDetail
import com.ng.rapetracker.room.TableNames.Companion.RAPE_DETAIL_TABLE

/**
 * Defines methods for using the SleepNight class with Room.
 */
@Dao
interface RapeDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRapeDetail(list: List<RapeDetail>)//upsert -> Insert and/or update

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param night new value to write
     */
    @Update
    fun updateRapeDetail(rapeDetail: RapeDetail)


    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM $RAPE_DETAIL_TABLE")
    fun clearAllRapeDetails()

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by start time in descending order.
     */
    @Query("SELECT * FROM $RAPE_DETAIL_TABLE ORDER BY id DESC")
    fun getAllRapeDetail(): LiveData<List<RapeDetail>>

    @Query("SELECT * FROM $RAPE_DETAIL_TABLE  WHERE user_id = :id ORDER BY id DESC")
    fun getUserRapeDetail(id: Long): LiveData<List<RapeDetail>>
    @Query("SELECT * FROM $RAPE_DETAIL_TABLE  WHERE rape_support_type = :id ORDER BY id DESC")
    fun getSupportRapeDetail(id: Long): LiveData<List<RapeDetail>>

    /**
     * Selects and returns the row that matches the supplied start time, which is our key.
     *
     * @param key startTimeMilli to match
     */
    @Query("SELECT * from $RAPE_DETAIL_TABLE WHERE id = :id")
    fun getRapeDetailById(id: Long): RapeDetail?

    /**
     * Selects and returns the latest night.
     */
    @Query("SELECT * FROM $RAPE_DETAIL_TABLE ORDER BY id DESC LIMIT 1")
    fun getRecentRapeDetail(): LiveData<RapeDetail?>

}

