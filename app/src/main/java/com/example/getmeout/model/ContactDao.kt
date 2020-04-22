package com.example.getmeout.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContactDao {
    @Query("SELECT * FROM contacts_table ORDER BY selected DESC")
    fun getAll(): LiveData<List<Contact>>

    @Query("SELECT * FROM contacts_table ORDER BY uid DESC")
    fun getAllContacts(): List<Contact>

    @Query("SELECT * FROM contacts_table WHERE selected == 1 ORDER BY uid DESC")
    fun getAllSelected(): List<Contact>

    @Query("UPDATE contacts_table SET selected = :contact_sel WHERE uid == :contact_id")
    fun update(contact_sel: Boolean, contact_id: Int)

    @Insert
    fun insertAll(vararg contacts: Contact)

    @Delete
    fun delete(vararg contact: Contact)

    @Query("DELETE FROM contacts_table")
    fun deleteAll()
}