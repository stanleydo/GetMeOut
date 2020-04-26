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

    fun select( msg_id: Int) {
        messageDao.select(msg_id)
    }

    fun getSelected(): List<Message>{
        return messageDao.getSelected()
    }

    fun deleteByUid(msg_id: Int) {
        messageDao.deleteByUid(msg_id)
    }

    fun updateMessage(title: String, message: String, msg_id: Int) {
        messageDao.updateMessage(title, message, msg_id)
    }

}