package com.example.getmeout.view

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.getmeout.R
import com.example.getmeout.adapters.ContactAdapter
import com.example.getmeout.databinding.FragmentEditContactsBinding
import com.example.getmeout.model.Contact
import com.example.getmeout.viewmodel.ContactViewModel
import kotlinx.android.synthetic.main.fragment_edit_contacts.*
import kotlinx.android.synthetic.main.popup_msg.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.random.Random

class EditContacts : Fragment(){

    private lateinit var contactViewModel: ContactViewModel

    final val firstNameError : String = "Invalid First Name!"
    final val lastNameError : String = "Invalid Last Name!"
    final val phoneNumberError : String = "Invalid Phone Number!"

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

        var delete_on = false
        var edit_on = false

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

        contactAdapter.onItemClick = { contact ->
            GlobalScope.launch {
                contactViewModel.update(!contact.selected, contact.uid)
            }
        }

        binding.addContactBtn.setOnClickListener {
            addContact(contactViewModel)
        }

        binding.delContactBtn.setOnClickListener {
            delete_on = !delete_on
            edit_on = false

            if (delete_on) {
                del_contact_btn.setTextColor(Color.parseColor("#ff0000"))
                edit_contact_btn.setTextColor(Color.parseColor("#ffffff"))
                contactAdapter.onItemClick = { contact ->
                    GlobalScope.launch {
                        contactViewModel.deleteByUid(contact.uid)
                    }
                }
            } else {
                del_contact_btn.setTextColor(Color.parseColor("#ffffff"))
                contactAdapter.onItemClick = { contact ->
                    GlobalScope.launch {
                        contactViewModel.update(!contact.selected, contact.uid)
                    }
                }
            }
        }

        binding.editContactBtn.setOnClickListener {
            edit_on = !edit_on
            delete_on = false

            if (edit_on) {
                edit_contact_btn.setTextColor(Color.parseColor("#ff0000"))
                del_contact_btn.setTextColor(Color.parseColor("#ffffff"))
                contactAdapter.onItemClick = { contact ->
                    editContact(contactViewModel, contact.firstName, contact.lastName, contact.phoneNumber, contact.uid)
                }
            } else {
                edit_contact_btn.setTextColor(Color.parseColor("#ffffff"))
                contactAdapter.onItemClick = { contact ->
                    GlobalScope.launch {
                        contactViewModel.update(!contact.selected, contact.uid)
                    }
                }
            }
        }

        binding.importContactsBtn.setOnClickListener {
        }

        return binding.root
    }

    fun addContact(contactViewModel: ContactViewModel) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Add Contact")

        val firstname = EditText(context)
        val lastname = EditText(context)
        val phoneno = EditText(context)

        firstname.hint = "First Name"
        lastname.hint = "Last Name"
        phoneno.hint = "Phone Number"

        val linLayout: LinearLayout = LinearLayout(context)
        linLayout.orientation = LinearLayout.VERTICAL

        linLayout.addView(firstname)
        linLayout.addView(lastname)
        linLayout.addView(phoneno)

        builder.setView(linLayout)

        builder.setPositiveButton("Add") { dialog, which ->
            val input_fname = firstname.text.toString()
            val input_lname = lastname.text.toString()
            val input_phoneno = phoneno.text.toString()

            if (!checkFirstName(input_fname)) {
                popupMessage("Error", firstNameError)
            } else if (!checkLastName(input_lname)) {
                popupMessage("Error", lastNameError)
            } else if (!checkPhoneNum(input_phoneno)) {
                popupMessage("Error", phoneNumberError)
            } else {
                GlobalScope.launch {
                    contactViewModel.insert(Contact(0, input_fname, input_lname, input_phoneno, false))
                }
            }

        }

        builder.setNeutralButton("Cancel") { _, _ ->
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun editContact(contactViewModel: ContactViewModel, pre_fname:String, pre_lname:String, pre_phone:String, contact_id: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Edit Contact")

        val firstname = EditText(context)
        val lastname = EditText(context)
        val phoneno = EditText(context)

        firstname.hint = "First Name"
        lastname.hint = "Last Name"
        phoneno.hint = "Phone Number"

        firstname.setText(pre_fname)
        lastname.setText(pre_lname)
        phoneno.setText(pre_phone)

        val linLayout: LinearLayout = LinearLayout(context)
        linLayout.orientation = LinearLayout.VERTICAL

        linLayout.addView(firstname)
        linLayout.addView(lastname)
        linLayout.addView(phoneno)

        builder.setView(linLayout)

        builder.setPositiveButton("Save") { dialog, which ->
            val input_fname = firstname.text.toString()
            val input_lname = lastname.text.toString()
            val input_phoneno = phoneno.text.toString()

            if (!checkFirstName(input_fname)) {
                popupMessage("Error", firstNameError)
            } else if (!checkLastName(input_lname)) {
                popupMessage("Error", lastNameError)
            } else if (!checkPhoneNum(input_phoneno)) {
                popupMessage("Error", phoneNumberError)
            } else {
                GlobalScope.launch {
                    contactViewModel.updateContact(input_fname, input_lname, input_phoneno, contact_id)
                }
            }

        }

        builder.setNeutralButton("Cancel") { _, _ ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun checkFirstName(inputVal: String): Boolean{
        var result = true
        if(inputVal.length > 15 || inputVal.isBlank() || inputVal.isEmpty() ){
            result = false
        }
        return result
    }

    private fun checkLastName(inputVal: String): Boolean{
        var result = true
        if(inputVal.length > 15 || inputVal.isBlank() || inputVal.isEmpty() ){
            result = false
        }
        return result
    }

    private fun checkPhoneNum(inputVal: String): Boolean{
        var numeric = true

        if(inputVal.length in 6..15 && !inputVal.isBlank() && inputVal.isNotEmpty()) {
            try {
                val num = inputVal.toLong()
            }
            catch (e: NumberFormatException) {
                numeric = false
            }
        }
        else {
            numeric = false
        }
        return numeric
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
