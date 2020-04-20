package com.example.getmeout.model

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.room.Dao

class ContactRepository(private val contactDao: ContactDao) {

    val allContacts: LiveData<List<Contact>> = contactDao.getAll()

    fun insert(contact: Contact) {
        contactDao.insertAll(contact)
    }

    // TODO -- Delete via UID / PRI KEY
    fun delete(contact: Contact) {
        contactDao.delete(contact)
    }

    fun deleteAll() {
        contactDao.deleteAll()
    }

}