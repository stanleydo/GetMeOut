package com.example.getmeout.view

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
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

        val new_message: Message = Message(0,"SOS #1","HEY! IM HERE!", false)

        messageViewModel = ViewModelProvider(this).get(MessageViewModel::class.java)
        messageViewModel.getAll().observe(viewLifecycleOwner, Observer { messages -> messages?.let {messageAdapter.setMessages(it)}})

        messageAdapter.onItemClick = { message ->
            GlobalScope.launch {
                messageViewModel.select(message.uid)
            }
        }

        binding.addBtn.setOnClickListener {
            addMessage(messageViewModel)
        }

        binding.delBtn.setOnClickListener {
            GlobalScope.launch {
                messageViewModel.deleteAllMessages()
            }
        }

        return binding.root
    }

    fun addMessage(messageViewModel: MessageViewModel) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Add a Message")

        val msg_title = EditText(context)
        val msg_txt = EditText(context)

        msg_title.hint = "Message #1"
        msg_txt.hint = "Help!"

        val linLayout: LinearLayout = LinearLayout(context)
        linLayout.orientation = LinearLayout.VERTICAL

        linLayout.addView(msg_title)
        linLayout.addView(msg_txt)

        builder.setView(linLayout)

        builder.setPositiveButton("Add") { dialog, which ->
            val input_title = msg_title.text.toString()
            val input_txt = msg_txt.text.toString()

            GlobalScope.launch {
                messageViewModel.insert(Message(0, input_title, input_txt, true))
            }

        }

        builder.setNeutralButton("Cancel") { _, _ ->
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}