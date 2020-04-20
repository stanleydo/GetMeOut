package com.example.getmeout.view

import android.os.Bundle
import android.provider.Settings
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
import kotlinx.coroutines.launch

// TODO - Add Recyclerview stuff

class EditContacts : Fragment() {

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

        val db = Room.databaseBuilder(
            this.context!!, AppDatabase::class.java, "main-database").build()

        val new_contact: Contact = Contact(uid=0, firstName = "Stanley", lastName = "Do", phoneNumber = "1234567890", selected = false)

        contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
        contactViewModel.allContacts.observe(viewLifecycleOwner, Observer { contacts -> contacts?.let {contactAdapter.setContacts(it)}})

        binding.addContactBtn.setOnClickListener {
            GlobalScope.launch {
                contactViewModel.insert(new_contact)
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
