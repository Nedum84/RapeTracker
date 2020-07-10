package com.ng.rapetracker.room

import androidx.room.*
import com.ng.rapetracker.model.Country
import com.ng.rapetracker.model.RapeType

@Dao
interface CountryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upSertCountry(list: List<Country>)


    @Query("SELECT * from ${TableNames.COUNTRY} WHERE id = :id")
    fun getCountryById(id: Long): Country?


    @Query("SELECT * FROM ${TableNames.COUNTRY} ORDER BY id DESC")
    suspend fun getAllCountry(): List<Country>

}