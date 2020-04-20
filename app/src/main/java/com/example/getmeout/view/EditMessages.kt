package com.example.getmeout.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.getmeout.R
import com.example.getmeout.adapters.MessageAdapter
import com.example.getmeout.databinding.FragmentEditMessagesBinding
import com.example.getmeout.model.AppDatabase
import com.example.getmeout.model.Message
import com.example.getmeout.viewmodel.MessageViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO - Add Recyclerview stuff

class EditMessages : Fragment() {

    private lateinit var messageViewModel: MessageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentEditMessagesBinding>(inflater,
            R.layout.fragment_edit_messages,container,false)
        // Inflate the layout for this fragment

        val recyclerView = binding.messagesRecyclerview
        val messageAdapter = MessageAdapter(this.context!!)
        recyclerView.adapter = messageAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.context!!)

        val db = Room.databaseBuilder(
            this.context!!, AppDatabase::class.java, "main-database").build()

        val new_message: Message = Message(0,"HEY! IM HERE!")

        messageViewModel = ViewModelProvider(this).get(MessageViewModel::class.java)
        messageViewModel.allMessages.observe(viewLifecycleOwner, Observer { messages -> messages?.let {messageAdapter.setMessages(it)}})

        binding.addMsg.setOnClickListener {
            GlobalScope.launch {
                messageViewModel.insert(new_message)
            }
        }

        binding.delMsg.setOnClickListener {
            GlobalScope.launch {
                messageViewModel.deleteAllMessages()
            }
        }

        return binding.root
    }


}