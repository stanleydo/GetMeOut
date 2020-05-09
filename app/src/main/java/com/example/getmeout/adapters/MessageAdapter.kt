package com.example.getmeout.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.getmeout.R
import com.example.getmeout.model.Message
import kotlinx.android.synthetic.main.message_item.view.*

class MessageAdapter internal constructor(context: Context) : RecyclerView.Adapter<MessageAdapter.MessageHolder>() {

    final val txt_selected_color = "#009600"
    final val txt_unselected_color = "#000000"

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private var messages: List<Message> = ArrayList()

    var onItemClick: ((Message) -> Unit)? = null
    var drawable: Drawable? = context.getDrawable(R.drawable.ic_check_circle_green_24dp)

    inner class MessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val msg_title: TextView = itemView.msg_title
        val msg_text: TextView = itemView.msg_txt

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(messages[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        val itemView = inflater.inflate(R.layout.message_item, parent, false)
        return MessageHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        val current = messages[position]
        holder.msg_title.text = current.title
        holder.msg_text.text = current.message

        if (current.selected) {
            holder.msg_title.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
            holder.msg_title.setTextColor(Color.parseColor(txt_selected_color))
            holder.msg_text.setTextColor(Color.parseColor(txt_selected_color))
        } else {
            holder.msg_title.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            holder.msg_title.setTextColor(Color.parseColor(txt_unselected_color))
            holder.msg_text.setTextColor(Color.parseColor(txt_unselected_color))
        }

    }

    internal fun setMessages(messages: List<Message>) {
        this.messages = messages
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return messages.size
    }


}