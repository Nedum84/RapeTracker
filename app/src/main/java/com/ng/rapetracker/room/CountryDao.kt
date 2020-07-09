package com.ng.rapetracker.room

import androidx.room.*
import com.ng.rapetracker.model.Country

@Dao
interface CountryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upSertCountry(vararg country: Country)


    @Query("SELECT * from ${TableNames.COUNTRY} WHERE id = :_id")
    fun getCountryById(_id: Long): Country?


    @Query("SELECT * FROM ${TableNames.COUNTRY} ORDER BY id DESC")
    fun getAllCountry(): List<Country>

}