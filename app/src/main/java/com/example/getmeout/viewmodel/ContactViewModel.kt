package com.example.getmeout.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.getmeout.model.Contact
import com.example.getmeout.model.ContactRepository

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: ContactRepository = ContactRepository(application)
    private var allContacts: LiveData<List<Contact>> = repository.getAllContacts()

    fun insert(contact: Contact) {
        repository.insert(contact)
    }

    fun deleteAllContacts() {
        repository.deleteAll()
    }

    fun getAllContacts(): LiveData<List<Contact>> {
        return allContacts
    }
}