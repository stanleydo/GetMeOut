package com.example.getmeout.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContactDao {
    @Query("SELECT * FROM contacts_table ORDER BY uid ASC")
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

    @Query("DELETE FROM contacts_table WHERE uid == :contact_id")
    fun deleteByUid(contact_id: Int)

    @Query("DELETE FROM contacts_table")
    fun deleteAll()

    @Query("UPDATE contacts_table SET first_name = :first_name, last_name = :last_name, phone_number = :phone_no WHERE uid == :contact_id")
    fun updateContact(first_name: String, last_name: String, phone_no: String, contact_id: Int)
}