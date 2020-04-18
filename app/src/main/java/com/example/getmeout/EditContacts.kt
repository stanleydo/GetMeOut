package com.example.getmeout

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.getmeout.database.AppDatabase
import com.example.getmeout.database.Contact
import com.example.getmeout.databinding.FragmentEditContactsBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO - Add Recyclerview stuff

class EditContacts : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentEditContactsBinding>(inflater,
            R.layout.fragment_edit_contacts,container,false)

        val db = Room.databaseBuilder(
            this.context!!, AppDatabase::class.java, "main-database").build()

        val new_contact: Contact = Contact(uid=0, firstName = "Stanley", lastName = "Do", phoneNumber = "1234567890", selected = false)

        binding.addContactBtn.setOnClickListener {
            GlobalScope.launch {
                db.ContactsDatabaseDao.insertAll(new_contact)
            }
            Toast.makeText(this.context!!, "Added", Toast.LENGTH_SHORT).show()
        }

        binding.delContactBtn.setOnClickListener {
            GlobalScope.launch {
                db.ContactsDatabaseDao.deleteAll()
            }
            Toast.makeText(this.context!!, "Deleted", Toast.LENGTH_SHORT).show()
        }

        binding.editContactBtn.setOnClickListener {
            GlobalScope.launch {
                for (contact in db.ContactsDatabaseDao.getAll()) {
                    Log.e("ERROR", contact.firstName)
                    quickToast(contact.firstName)
                }
            }
        }

        return binding.root
    }

    fun quickToast(message: String) {
        Toast.makeText(this.context!!, message, Toast.LENGTH_SHORT).show()
    }


}
