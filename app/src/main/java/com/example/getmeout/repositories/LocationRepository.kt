package com.example.getmeout.repositories

import androidx.lifecycle.LiveData
import com.example.getmeout.model.Location
import com.example.getmeout.model.LocationDao

class LocationRepository (private val locationDao: LocationDao) {

    fun getLocation(): LiveData<List<Location>> {
        return locationDao.getLocation()
    }

    fun updateLocation() {
        locationDao.updateLocation()
    }

    fun getLocation_value(): List<Location> {
        return locationDao.getLocation_value()
    }

    fun insert(location: Location) {
        locationDao.insert(location)
    }

}