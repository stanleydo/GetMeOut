package com.example.getmeout.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.getmeout.model.AppDatabase
import com.example.getmeout.model.Location
import com.example.getmeout.repositories.LocationRepository

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LocationRepository

    init {
        val dao = AppDatabase.getInstance(application).LocationDatabaseDao()
        repository = LocationRepository(dao)
    }

    fun getAll(): LiveData<List<Location>> {
        return repository.getLocation()
    }

    fun getLocation_value(): Location {
        return repository.getLocation_value()[0]
    }

    fun updateLocation() {
        repository.updateLocation()
    }

    fun insert(location: Location) {
        repository.insert(location)
    }


}