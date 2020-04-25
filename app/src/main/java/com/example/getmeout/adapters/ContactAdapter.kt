package com.example.getmeout.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.getmeout.R
import com.example.getmeout.model.Contact
import kotlinx.android.synthetic.main.contact_item.view.*

class ContactAdapter internal constructor(context: Context) : RecyclerView.Adapter<ContactAdapter.ContactHolder>() {

    final val bg_selected_color = "#0236C4"
    final val bg_unselected_color = "#4F7BF5"
    final val txt_selected_color = "#16FF16"
    final val txt_unselected_color = "#A5A5A5"

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var contacts: List<Contact> = ArrayList()
    var onItemClick: ((Contact) -> Unit)? = null

    inner class ContactHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val firstName: TextView = itemView.item_firstname
        val lastname: TextView = itemView.item_lastname
        val phoneno: TextView = itemView.item_phoneno

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(contacts[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val itemView = inflater.inflate(R.layout.contact_item, parent, false)
        return ContactHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        val current = contacts[position]
        holder.firstName.text = current.firstName
        holder.lastname.text = current.lastName
        holder.phoneno.text = current.phoneNumber

        if (current.selected) {
            holder.itemView.setBackgroundColor(Color.parseColor(bg_selected_color))
            holder.firstName.setTextColor(Color.parseColor(txt_selected_color))
            holder.lastname.setTextColor(Color.parseColor(txt_selected_color))
            holder.phoneno.setTextColor(Color.parseColor(txt_selected_color))
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor(bg_unselected_color))
            holder.firstName.setTextColor(Color.parseColor(txt_unselected_color))
            holder.lastname.setTextColor(Color.parseColor(txt_unselected_color))
            holder.phoneno.setTextColor(Color.parseColor(txt_unselected_color))
        }
    }

    internal fun setContacts(contacts: List<Contact>) {
        this.contacts = contacts
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return contacts.size
    }
}