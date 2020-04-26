package com.example.getmeout.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.getmeout.model.AppDatabase
import com.example.getmeout.model.Message
import com.example.getmeout.model.MessageRepository

class MessageViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MessageRepository

    init {
        val dao = AppDatabase.getInstance(application).MessagesDatabaseDao()
        repository = MessageRepository(dao)
    }

    fun getAll(): LiveData<List<Message>> {
        return repository.getAll()
    }

    fun insert(message: Message) {
        repository.insert(message)
    }

    fun delete(message: Message) {
        repository.delete(message)
    }

    fun deleteAllMessages() {
        repository.deleteAll()
    }

    fun getAllMessage_VALUES(): List<Message> {
        return repository.getAllMessage_VALUES()
    }

    fun select(msg_id: Int) {
        repository.select(msg_id)
    }

    fun getSelected(): List<Message>{
        return repository.getSelected()
    }

    fun deleteByUid(msg_id: Int) {
        repository.deleteByUid(msg_id)
    }

    fun updateMessage(title: String, message: String, msg_id: Int) {
        repository.updateMessage(title, message, msg_id)
    }

}