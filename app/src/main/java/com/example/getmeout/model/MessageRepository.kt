package com.example.getmeout.model

import androidx.lifecycle.LiveData

class MessageRepository(private val messageDao: MessageDao) {

    fun getAll(): LiveData<List<Message>> {
        return messageDao.getAll()
    }

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

    fun getAllMessage_VALUES(): List<Message> {
        return messageDao.getAllMessages()
    }

}