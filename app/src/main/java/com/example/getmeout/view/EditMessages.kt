package com.example.getmeout.view

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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
import kotlinx.android.synthetic.main.fragment_edit_messages.*
import kotlinx.android.synthetic.main.popup_msg.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO - Add Recyclerview stuff

class EditMessages : Fragment() {

    private lateinit var messageViewModel: MessageViewModel
    final val messageTitleError : String = "Invalid Title!"
    final val messageError : String = "Invalid Message!"

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

        var delete_on = false
        var edit_on = false

        messageAdapter.onItemClick = {message ->
            GlobalScope.launch {
                messageViewModel.select(message.uid)
            }
        }

        binding.addBtn.setOnClickListener {
            addMessage(messageViewModel)
        }

        binding.delBtn.setOnClickListener {
            delete_on = !delete_on
            edit_on = false

            if (delete_on) {
                del_btn.setTextColor(Color.parseColor("#ff0000"))
                edit_btn.setTextColor(Color.parseColor("#ffffff"))
                messageAdapter.onItemClick = { message ->
                    GlobalScope.launch {
                        messageViewModel.deleteByUid(message.uid)
                    }
                }
            } else {
                del_btn.setTextColor(Color.parseColor("#ffffff"))
                messageAdapter.onItemClick = { message ->
                    GlobalScope.launch {
                        messageViewModel.select(message.uid)
                    }
                }
            }
        }

        binding.editBtn.setOnClickListener {
            edit_on = !edit_on
            delete_on = false

            if (edit_on) {
                edit_btn.setTextColor(Color.parseColor("#ff0000"))
                del_btn.setTextColor(Color.parseColor("#ffffff"))
                messageAdapter.onItemClick = { message ->
                    editMessage(messageViewModel, message.title, message.message, message.uid)
                }
            } else {
                edit_btn.setTextColor(Color.parseColor("#ffffff"))
                messageAdapter.onItemClick = { message ->
                    GlobalScope.launch {
                        messageViewModel.select(message.uid)
                    }
                }
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

            if (!checkTitle(input_title)) {
                popupMessage("Error", messageTitleError)
            } else if (!checkMessage(input_txt)) {
                popupMessage("Error", messageError)
            } else {
                GlobalScope.launch {
                    messageViewModel.insert(Message(0, input_title, input_txt, false))
                }
            }

        }

        builder.setNeutralButton("Cancel") { _, _ ->
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun editMessage(messageViewModel: MessageViewModel, pre_title:String, pre_message:String, msg_id: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Edit Contact")

        val title = EditText(context)
        val msg = EditText(context)

        title.hint = "First Name"
        msg.hint = "Last Name"

        title.setText(pre_title)
        msg.setText(pre_message)

        val linLayout: LinearLayout = LinearLayout(context)
        linLayout.orientation = LinearLayout.VERTICAL

        linLayout.addView(title)
        linLayout.addView(msg)

        builder.setView(linLayout)

        builder.setPositiveButton("Save") { dialog, which ->
            val input_title = title.text.toString()
            val input_msg = msg.text.toString()

            if (!checkTitle(input_title)) {
                popupMessage("Error", messageTitleError)
            } else if (!checkMessage(input_msg)) {
                popupMessage("Error", messageError)
            } else {
                GlobalScope.launch {
                    messageViewModel.updateMessage(input_title, input_msg, msg_id)
                }
            }

        }

        builder.setNeutralButton("Cancel") { _, _ ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun checkTitle(inputVal: String): Boolean{
        var result = true
        if(inputVal.length > 15 || inputVal.isBlank() || inputVal.isEmpty() ){
            result = false
        }
        return result
    }

    private fun checkMessage(inputVal: String): Boolean{
        var result = true
        if(inputVal.length > 160 || inputVal.isBlank() || inputVal.isEmpty() ){
            result = false
        }
        return result
    }

    private fun popupMessage(title: String, msg: String) {
        val dialog: Dialog = Dialog(context!!)
        dialog.setContentView(R.layout.popup_msg)

        val titletv: TextView = dialog.popup_title
        val msgtv: TextView = dialog.popup_msg

        titletv.setText(title)
        msgtv.setText(msg)

        dialog.show()
    }

}