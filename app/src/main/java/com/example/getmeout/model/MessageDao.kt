package com.example.getmeout.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages_table")
    fun getAll(): LiveData<List<Message>>

    @Insert
    fun insertAll(vararg messages: Message)

    @Delete
    fun delete(vararg message: Message)

    @Query("DELETE FROM messages_table")
    fun deleteAll()
}