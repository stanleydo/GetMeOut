package com.example.getmeout.view

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts.CONTENT_LOOKUP_URI
import android.provider.ContactsContract.Contacts.DISPLAY_NAME
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentResolverCompat
import androidx.core.content.ContentResolverCompat.query
import androidx.core.content.ContextCompat
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

    // Initialize the contact view model
    private lateinit var contactViewModel: ContactViewModel

    // Strings for errors
    final val firstNameError : String = "Invalid First Name!"
    final val lastNameError : String = "Invalid Last Name!"
    final val phoneNumberError : String = "Invalid Phone Number!"

    var MY_PERMISSIONS_REQUEST_READ_CONTACTS: Int = 0

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
                del_contact_btn.setTextColor(Color.parseColor("#ff0000")) // Red
                edit_contact_btn.setTextColor(Color.parseColor("#ffffff")) // White
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

        val contactsContract = ContactsContract()

        binding.importContactsBtn.setOnClickListener {
            if (!checkContactsPermissions()) {
                requestContactsPermissions()
            } else {
                importContacts()
            }
        }

        return binding.root
    }

    fun importContacts() {
        var contentResolver = this.context!!.contentResolver
        var cursor: Cursor? = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null, null)
        var numLoops = 0

        if (cursor == null || !cursor.moveToNext()) {
            cursor!!.close()
            return
        }

        while (!cursor!!.isAfterLast) {

            var name = ""
            var number = ""

            // Col 5 is display name
            // Col 50 is phone number
//            var id = cursor.getString(cursor.getColumnIndex(ContactsContract.ContactsColumns.DISPLAY_NAME))
            if (cursor.getString(5) != null && cursor.getString(50) != null) {
                name = cursor.getString(5)
                number = cursor.getString(50).replace(Regex("""[- ]"""), "")
                GlobalScope.launch {
                    contactViewModel.insert(Contact(0, name, "", number, false))
                }
            }

            cursor.moveToNext()
        }

        cursor!!.close()

        // TEST LOOP FOR COLUMN NAMES.
        /*
        for (i in 0..cursor!!.columnCount-1) {
            Log.e("ERROR", "NAME: " + name + " i: "+ i + " " + cursor.getString(i) + " " + cursor.getColumnName(i))
        }
        */
    }

    fun addContact(contactViewModel: ContactViewModel) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Add Contact")

        val firstname = EditText(context)
        val lastname = EditText(context)
        val phoneno = EditText(context)

        firstname.hint = "First Name"
        lastname.hint = "Last Name (Optional)"
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
        lastname.hint = "Last Name (Optional)"
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

    // Optional - I don't think we need this anymore.
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

    // Checks to see if the you have the Contacts Permission.
    // This will return TRUE if permission has been granted.
    fun checkContactsPermissions(): Boolean {
        return (ContextCompat.checkSelfPermission(this.context!!, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
    }

    // This method requests the permission for Contacts
    fun requestContactsPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity!!,
                Manifest.permission.READ_CONTACTS)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this.activity!!,
                arrayOf(Manifest.permission.READ_CONTACTS),
                MY_PERMISSIONS_REQUEST_READ_CONTACTS)

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    // This method is called when the user responds to the permission request.
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_CONTACTS -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

}
