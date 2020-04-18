package com.example.getmeout.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ContactDao {
    @Query("SELECT * FROM contacts_table")
    fun getAll(): MutableList<Contact>

    @Insert
    fun insertAll(vararg contacts: Contact)

    @Delete
    fun delete(contact: Contact)

    @Query("DELETE FROM contacts_table")
    fun deleteAll()
}