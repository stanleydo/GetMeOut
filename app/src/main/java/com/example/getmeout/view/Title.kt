package com.example.getmeout.view


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Message
import android.provider.Settings
import android.telephony.SmsManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.room.Room
import com.example.getmeout.R
import com.example.getmeout.view.TitleDirections
import com.example.getmeout.databinding.FragmentTitleBinding
import com.example.getmeout.model.AppDatabase
import com.example.getmeout.model.Contact
import com.example.getmeout.model.ContactDao
import com.example.getmeout.viewmodel.ContactViewModel
import com.example.getmeout.viewmodel.MessageViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class Title : Fragment() {

    // Initialize an integer
    // This is how you initialize a variable in Kotlin
    // VAR means that the data is mutable, while VAL means that the data is NOT mutable.
    // You can statically type the variable using a colon : as show below.
    var MY_PERMISSIONS_REQUEST_SEND_SMS: Int = 0

    private lateinit var contactViewModel: ContactViewModel
    private lateinit var messageViewModel: MessageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentTitleBinding>(inflater,
            R.layout.fragment_title,container,false)
        // Inflate the layout for this fragment

        // Uses navigation to navigate from the Title Fragment to the Settings Fragment.
        binding.settingsBtn.setOnClickListener { view: View ->
            view.findNavController().navigate(TitleDirections.actionTitleToSettings())
        }

        contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
        messageViewModel = ViewModelProvider(this).get(MessageViewModel::class.java)

        binding.sendBtn.setOnClickListener {

            GlobalScope.launch {
                var all_contacts_values = contactViewModel.getAllSelected()
                var message = messageViewModel.getAllMessage_VALUES()[0].message

                val smsManager = SmsManager.getDefault()
                    for (contact in all_contacts_values) {
                        sendSMS(contact.phoneNumber, message, smsManager = smsManager)
                    }

                }

            Toast.makeText(this.context, "Sent!", Toast.LENGTH_SHORT).show()
        }

        return binding.root

    }

    fun sendSMS(phoneNo: String, msg: String, smsManager: SmsManager) {
        try {
            if (!checkSMSPermissions()) {
                requestSMSPermissions()
                sendSMS(phoneNo, msg, smsManager)
            } else {
                smsManager.sendTextMessage(phoneNo, null, msg, null, null)
            }
        } catch (ex: Exception) {
            // Log.e will show up as an Error in your logcat with the exception's message.
            Log.e("ERROR", ex.message)
        }
    }

    // Checks to see if the you have the SEND_SMS Permission.
    // This will return TRUE if permission has been granted.
    fun checkSMSPermissions(): Boolean {
        return (ContextCompat.checkSelfPermission(this.context!!, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
    }

    // This method requests the permission for SEND_SMS
    fun requestSMSPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity!!,
                Manifest.permission.SEND_SMS)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this.activity!!,
                arrayOf(Manifest.permission.SEND_SMS),
                MY_PERMISSIONS_REQUEST_SEND_SMS)

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    // This method is called when the user responds to the permission request.
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_SEND_SMS -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
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
