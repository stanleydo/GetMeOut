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

    @Query("SELECT * FROM messages_table ORDER BY uid DESC")
    fun getAllMessages(): List<Message>

    @Insert
    fun insertAll(vararg message: Message)

    @Delete
    fun delete(vararg message: Message)

    @Query("DELETE FROM messages_table WHERE uid == :msg_id")
    fun deleteByUid(msg_id: Int)

    @Query("DELETE FROM messages_table")
    fun deleteAll()

    @Query("UPDATE messages_table SET selected = CASE uid WHEN :msg_id THEN 1 ELSE 0 END")
    fun select(msg_id: Int)

    @Query("SELECT * FROM messages_table WHERE selected = 1")
    fun getSelected(): List<Message>

    @Query("UPDATE messages_table SET title = :title, message = :message WHERE uid == :msg_id")
    fun updateMessage(title: String, message: String, msg_id: Int)
}