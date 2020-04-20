package com.example.getmeout.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.getmeout.model.AppDatabase
import com.example.getmeout.model.Contact
import com.example.getmeout.model.ContactRepository
import kotlinx.coroutines.launch

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ContactRepository
    val allContacts: LiveData<List<Contact>>

    init {
        val dao = AppDatabase.getInstance(application).ContactsDatabaseDao()
        repository = ContactRepository(dao)
        allContacts = repository.allContacts
    }

    fun insert(contact: Contact) {
            repository.insert(contact)
    }

    fun delete(contact: Contact) {
            repository.delete(contact)
    }

    fun deleteAllContacts() {
            repository.deleteAll()
    }

}