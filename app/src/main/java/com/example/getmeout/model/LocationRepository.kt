package com.example.getmeout.model

import androidx.lifecycle.LiveData

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