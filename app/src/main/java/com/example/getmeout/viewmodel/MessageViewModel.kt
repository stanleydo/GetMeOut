package com.example.getmeout.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.getmeout.model.AppDatabase
import com.example.getmeout.model.Message
import com.example.getmeout.model.MessageRepository

class MessageViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MessageRepository
    val allMessages: LiveData<List<Message>>

    init {
        val dao = AppDatabase.getInstance(application).MessagesDatabaseDao()
        repository = MessageRepository(dao)
        allMessages = repository.allContacts
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

}