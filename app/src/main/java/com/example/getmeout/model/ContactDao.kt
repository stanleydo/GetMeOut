package com.example.getmeout.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ContactDao {
    @Query("SELECT * FROM contacts_table")
    fun getAll(): LiveData<List<Contact>>

    @Insert
    fun insertAll(vararg contacts: Contact)

    @Delete
    fun delete(vararg contact: Contact)

    @Query("DELETE FROM contacts_table")
    fun deleteAll()
}