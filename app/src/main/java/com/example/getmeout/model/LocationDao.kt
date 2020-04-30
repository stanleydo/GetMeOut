package com.example.getmeout.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDao {

    @Query("SELECT * FROM location_table WHERE uid == 1")
    fun getLocation(): LiveData<List<Location>>

    @Query("SELECT * FROM location_table WHERE uid == 1")
    fun getLocation_value(): List<Location>

    @Query("UPDATE location_table SET status = not status WHERE uid == 1")
    fun updateLocation()

    @Insert
    fun insert(Location: Location)

}