package com.example.getmeout.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.getmeout.model.AppDatabase
import com.example.getmeout.model.Contact
import com.example.getmeout.model.ContactRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ContactRepository

    init {
        val dao = AppDatabase.getInstance(application).ContactsDatabaseDao()
        repository = ContactRepository(dao)
    }

    fun getAll(): LiveData<List<Contact>> {
        return repository.getAll()
    }

    fun update(selected: Boolean, contact_uid: Int) {
        repository.update(selected, contact_uid)
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

    fun getAllContacts_VALUES(): List<Contact> {
        return repository.getAllContacts_VALUES()
    }

    fun getAllSelected(): List<Contact> {
        return repository.getAllSelected()
    }

}