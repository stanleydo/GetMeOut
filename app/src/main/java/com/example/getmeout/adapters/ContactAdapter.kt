package com.example.getmeout.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.getmeout.R
import com.example.getmeout.model.Contact
import kotlinx.android.synthetic.main.contact_item.view.*
import org.w3c.dom.Text

class ContactAdapter : RecyclerView.Adapter<ContactAdapter.ContactHolder>() {

    private var contacts: List<Contact> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.contact_item, parent, false)
        return ContactHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContactAdapter.ContactHolder, position: Int) {
        val currentContact: Contact = contacts[position]
        holder.item_firstname.text = currentContact.firstName
        holder.item_lastname.text = currentContact.lastName
        holder.item_phoneno.text = currentContact.phoneNumber
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    fun setContacts(contacts: List<Contact>) {
        this.contacts = contacts
        notifyDataSetChanged()
    }

    inner class ContactHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var item_firstname: TextView = itemView.item_firstname
        var item_lastname: TextView = itemView.item_lastname
        var item_phoneno: TextView = itemView.item_phoneno
    }

}