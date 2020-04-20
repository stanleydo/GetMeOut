package com.example.getmeout.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.getmeout.R
import com.example.getmeout.model.Message
import kotlinx.android.synthetic.main.message_item.view.*

class MessageAdapter internal constructor(context: Context) : RecyclerView.Adapter<MessageAdapter.MessageHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var messages: List<Message> = ArrayList()

    inner class MessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message: TextView = itemView.item_message
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        val itemView = inflater.inflate(R.layout.message_item, parent, false)
        return MessageHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        val current = messages[position]
        holder.message.text = current.message
    }

    internal fun setMessages(messages: List<Message>) {
        this.messages = messages
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}