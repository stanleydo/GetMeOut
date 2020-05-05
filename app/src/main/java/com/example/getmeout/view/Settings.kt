package com.example.getmeout.view


import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.getmeout.R
import com.example.getmeout.databinding.FragmentSettingsBinding
import com.example.getmeout.model.Location
import com.example.getmeout.viewmodel.LocationViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class Settings : Fragment() {

    private var location_on = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentSettingsBinding>(inflater,
            R.layout.fragment_settings,container,false)
        // Inflate the layout for this fragment

        // Initialize the drawables from res/drawable
        var loc_on_drawable: Drawable? = this.context!!.getDrawable(R.drawable.ic_location_on_white_36dp)
        var loc_off_drawable: Drawable? = this.context!!.getDrawable(R.drawable.ic_location_off_white_36dp)

        // Initialize the location view model to access the location flag
        val locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)

        // We can observe changes in the database and make actions as it happens.
        locationViewModel.getAll().observe(viewLifecycleOwner, Observer {
            // This checks if there's any location entry at all.
            // If there is, then we can assign "location_on" to the data value.
            if (it.isNotEmpty()) {
                // It refers to the list of locations we get from the observer.
                // We use it[0] because we always know that we will have only a single entry in the Locations DB.
                location_on = it[0].status
            } else {
                // If there isn't anything in the locations DB, let's make an entry.
                GlobalScope.launch {
                    locationViewModel.insert(Location(1, false))
                }
            }

            // This handles GUI changes based off of the entry.
            if (location_on) {
                binding.locationBtn.setText("LOCATION ON")
                binding.locationBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, loc_on_drawable, null)
                binding.locationBtn.background = this.context!!.getDrawable(R.drawable.gradient_btn_on)
            } else {
                binding.locationBtn.setText("LOCATION OFF")
                binding.locationBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, loc_off_drawable, null)
                binding.locationBtn.background = this.context!!.getDrawable(R.drawable.gradient_btn_off)
            }
        })


        // Button to navigate to the Edit Contacts page
        binding.selectContactsBtn.setOnClickListener { view: View ->
            view.findNavController().navigate(SettingsDirections.actionSettingsToEditContacts())
        }

        // Button to navigate to the Edit Contacts button
        binding.editMessagesBtn.setOnClickListener { view: View ->
            view.findNavController().navigate(SettingsDirections.actionSettingsToEditMessages())
        }

        // UpdateLocation basically just reverses the status of the single location entry.
        binding.locationBtn.setOnClickListener {
            GlobalScope.launch {
                locationViewModel.updateLocation()
            }
        }

        return binding.root
    }


}
