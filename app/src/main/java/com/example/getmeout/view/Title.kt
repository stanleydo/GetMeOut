package com.example.getmeout.view


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
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
import com.example.getmeout.R
import com.example.getmeout.databinding.FragmentTitleBinding
import com.example.getmeout.model.Contact
import com.example.getmeout.model.Message
import com.example.getmeout.viewmodel.ContactViewModel
import com.example.getmeout.viewmodel.LocationViewModel
import com.example.getmeout.viewmodel.MessageViewModel
import com.google.android.gms.location.*
import kotlinx.coroutines.*

private const val PERMISSION_REQUEST = 10

class Title : Fragment() {

    // Initialize an integer
    // This is how you initialize a variable in Kotlin
    // VAR means that the data is mutable, while VAL means that the data is NOT mutable.
    // You can statically type the variable using a colon : as show below.
    // These two integers identify permissions requests.
    val SEND_SMS_PERMISSIONS_ID: Int = 0
    val LOCATIONS_PERMISSIONS_ID: Int = 42

    // This is for the send notification.
    private var sendText: String = "No Contacts or Message Selected!"

    // Fused Location Provider API late initialization.
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    // View Models late inits for DB access.
    private lateinit var contactViewModel: ContactViewModel
    private lateinit var messageViewModel: MessageViewModel
    private lateinit var locationViewModel: LocationViewModel

    // This string is used to append to messages when Location is enabled.
    // If location isn't enabled, then it just stays empty.
    private var location_txt: String = ""

    // Each fragment has an onCreateView. It's called whenever it's navigated to.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // We use databinding so we don't have to use FindViewById()
        // We can just say binding.ID to get what we want.
        val binding = DataBindingUtil.inflate<FragmentTitleBinding>(inflater,
            R.layout.fragment_title,container,false)
        // Inflate the layout for this fragment

        // This refers to the button XML. We set an onclick listener to navigate to Settings.
        // Uses navigation to navigate from the Title Fragment to the Settings Fragment.
        binding.settingsBtn.setOnClickListener { view: View ->
            view.findNavController().navigate(TitleDirections.actionTitleToSettings())
        }

        // Initialize the FusedLocation Client
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.activity!!)

        // Initialize the view models
        // View models handle the display logic for the view and call functions from repositories.
        // They are also initialized with DAOs (Database Access Objects) which are passed into the
        // repositories. The repository then uses the DAO for DB reading or writing.
        contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
        messageViewModel = ViewModelProvider(this).get(MessageViewModel::class.java)
        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)

        // Coroutines are tough to understand - for now we will keep using Globalscope.
        // However, this is NOT good practice due to memory leak issues.
        // I'm not good enough with coroutines yet, so this will be a TODO.
        // A blocking coroutine which blocks the main thread until the jobs are completed
        fun send() = runBlocking {

            // Since we can not access the DB on the main thread,
            val job = GlobalScope.launch{
                var all_contacts_values: List<Contact> = contactViewModel.getAllSelected()
                var message: List<Message> = messageViewModel.getSelected()
                // Location is a list of Locations (Not statically typed due to import names conflicts)
                var location = locationViewModel.getLocation_value()

                if (all_contacts_values.isEmpty() or message.isEmpty()) {
                    sendText = "No Contacts or Message Selected!"
                } else {
                    sendText = "Messages Sent!"
                    var final_message = message[0].message
                    var location_on = false

                    if (location.isNotEmpty()) {
                        location_on = location[0].status
                    }

                    if (location_on) {
                        getLastLocation()

                        var timeout = 0

                        while (location_txt == "") {
                            if (timeout > 10) {
                                break
                            }
                            Thread.sleep(200)
                            timeout += 1
                        }

                        final_message += "\n" + location_txt
                    }
                    val smsManager = SmsManager.getDefault()
                    for (contact in all_contacts_values) {
                        sendSMS(contact.phoneNumber, final_message, smsManager = smsManager)
                    }
                }
            }
        }

        // Set the onclick listener to send the SMS.
        binding.sendBtn.setOnClickListener {
            send()
            Thread.sleep(1000)
            Toast.makeText(this.context, sendText, Toast.LENGTH_SHORT).show()
        }

        return binding.root

    }

    // Function that actually Sends an SMS.
    // SMS Manager is the thing that manages SMS operations on the device.
    fun sendSMS(phoneNo: String, msg: String, smsManager: SmsManager) {
        try {
            if (!checkSMSPermissions()) {
                requestSMSPermissions()
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
                SEND_SMS_PERMISSIONS_ID)

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    // This method is called when the user responds to the permission request.
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            SEND_SMS_PERMISSIONS_ID -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission was granted
                }
                return
            }

            LOCATIONS_PERMISSIONS_ID -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getLastLocation()
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

    // SAUL
    @SuppressLint("MissingPermission")
    private fun getLastLocation(){

        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this.activity!!) { task ->
                    var location: Location? = task.result
                    requestNewLocationData()
//
                }
            } else {
                Toast.makeText(this.context!!, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.activity!!)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        var locationString = "";

        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation

            val lat = mLastLocation.latitude.toString()
            val long = mLastLocation.longitude.toString()

            location_txt = "https://www.google.com/maps?q=${lat},${long}"

        }
    }
    //Check if location is enabled in the device
    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    //Check for location permissions have been allowed for the app
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this.context!!,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this.context!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
    //Request Location permissions
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this.activity!!,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            LOCATIONS_PERMISSIONS_ID
        )
    }


}
