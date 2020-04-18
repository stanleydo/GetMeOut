package com.example.getmeout.model

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

class ContactRepository(application: Application) {

    private lateinit var contactDao: ContactDao
    private lateinit var allContacts: LiveData<List<Contact>>

    init {
        val database : AppDatabase = AppDatabase.getInstance(
            application.applicationContext
        )!!
        contactDao = database.ContactsDatabaseDao
        allContacts = contactDao.getAll()
    }

    fun insert(contact: Contact) {
        val insertContactAsyncTask = InsertContactAsyncTask(contactDao).execute(contact)
    }

    // TODO -- Delete via UID / PRI KEY
    fun delete() {

    }

    fun deleteAll() {
        val deleteAllContactAsyncTask = DeleteAllContactsAsyncTask(contactDao).execute()
    }

    fun getAllContacts(): LiveData<List<Contact>> {
        return allContacts
    }

    private class InsertContactAsyncTask(contactDao: ContactDao) : AsyncTask<Contact, Unit, Unit>() {
        val contactDao = contactDao

        override fun doInBackground(vararg p0: Contact?) {
            contactDao.insertAll(p0[0]!!)
        }
    }

    private class DeleteAllContactsAsyncTask(val contactDao: ContactDao) : AsyncTask<Unit, Unit, Unit>() {
        override fun doInBackground(vararg p0: Unit?) {
            contactDao.deleteAll()
        }
    }
}