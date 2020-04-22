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

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var contacts: List<Contact> = ArrayList()
    val selected_color = "#008B00"
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
            holder.itemView.setBackgroundColor(Color.parseColor(selected_color))
        }
    }

    internal fun setContacts(contacts: List<Contact>) {
        this.contacts = contacts
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    interface OnContactListener {
        fun onContactClick(position: Int)
    }
}