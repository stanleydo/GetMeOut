package com.example.getmeout.model

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.room.Dao

class ContactRepository(private val contactDao: ContactDao) {

    fun getAll(): LiveData<List<Contact>> {
        return contactDao.getAll()
    }

    fun insert(contact: Contact) {
        contactDao.insertAll(contact)
    }

    fun update(selected: Boolean, contact_uid: Int) {
        contactDao.update(selected, contact_uid)
    }

    // TODO -- Delete via UID / PRI KEY
    fun delete(contact: Contact) {
        contactDao.delete(contact)
    }

    fun deleteAll() {
        contactDao.deleteAll()
    }

    fun getAllContacts_VALUES(): List<Contact> {
        return contactDao.getAllContacts()
    }

    fun getAllSelected(): List<Contact> {
        return contactDao.getAllSelected()
    }

}