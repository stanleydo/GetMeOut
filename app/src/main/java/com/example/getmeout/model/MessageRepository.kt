package com.example.getmeout.model

import androidx.lifecycle.LiveData

class MessageRepository(private val messageDao: MessageDao) {

    val allContacts: LiveData<List<Message>> = messageDao.getAll()

    fun insert(message: Message) {
        messageDao.insertAll(message)
    }

    // TODO -- Delete via UID / PRI KEY
    fun delete(message: Message) {
        messageDao.delete(message)
    }

    fun deleteAll() {
        messageDao.deleteAll()
    }

}