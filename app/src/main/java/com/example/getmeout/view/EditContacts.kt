package com.example.getmeout.view

import android.content.ContentValues.TAG
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.getmeout.R
import com.example.getmeout.adapters.ContactAdapter
import com.example.getmeout.databinding.FragmentEditContactsBinding
import com.example.getmeout.model.AppDatabase
import com.example.getmeout.model.Contact
import com.example.getmeout.viewmodel.ContactViewModel
import kotlinx.android.synthetic.main.fragment_title.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt

// TODO - Add Recyclerview stuff

class EditContacts : Fragment(){

    private lateinit var contactViewModel: ContactViewModel
//    private val adapter = ContactAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentEditContactsBinding>(inflater,
            R.layout.fragment_edit_contacts,container,false)

        val recyclerView = binding.contactsRecyclerview
        val contactAdapter = ContactAdapter(this.context!!)
        recyclerView.adapter = contactAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.context!!)

        // TODO -- REMOVE TEST CASES!!
        val new_contact: Contact = Contact(uid=0, firstName = "Stanley", lastName = "Do", phoneNumber = "1234567890", selected = true)
        val new_contact2: Contact = Contact(uid=0, firstName = "James", lastName = "Ochoa", phoneNumber = "16261231234", selected = true)
        val new_contact3: Contact = Contact(uid=0, firstName = "Tom", lastName = "Whiskey", phoneNumber = "0987654321", selected = true)

        val ran_contacts: MutableList<Contact> = ArrayList()
        var num_ran_contacts = 10

        for (i in 0..num_ran_contacts) {
            val random: Long = Random.nextLong(1000000000, 9999999999)
            val ran_contact: Contact = Contact(uid=0, firstName ="Name"+i, lastName="LastName"+i, phoneNumber="1"+random, selected=true)
            ran_contacts.add(ran_contact)
        }
        // TODO -- REMOVE TEST CASES!!

        contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
        contactViewModel.getAll().observe(viewLifecycleOwner, Observer { contacts -> contacts?.let {contactAdapter.setContacts(it)}})

        contactAdapter.onItemClick = {contact ->
            GlobalScope.launch {
                contactViewModel.update(!contact.selected, contact.uid)
            }
        }

        binding.addContactBtn.setOnClickListener {
            GlobalScope.launch {
                contactViewModel.insert(new_contact)
                contactViewModel.insert(new_contact2)
                contactViewModel.insert(new_contact3)
                for (contact in ran_contacts) {
                    contactViewModel.insert(contact)
                }
            }
        }

        binding.delContactBtn.setOnClickListener {
            GlobalScope.launch {
                contactViewModel.deleteAllContacts()
            }
        }

        binding.editContactBtn.setOnClickListener {
        }

        return binding.root
    }


}
