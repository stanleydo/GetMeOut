package com.example.getmeout.view


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.getmeout.R
import com.example.getmeout.view.SettingsDirections
import com.example.getmeout.databinding.FragmentSettingsBinding
import com.example.getmeout.model.AppDatabase
import com.example.getmeout.model.Location
import com.example.getmeout.model.LocationDao
import com.example.getmeout.model.LocationRepository
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


        // Button to navigate to the Edit Contacts page
        binding.selectContactsBtn.setOnClickListener { view: View ->
            view.findNavController().navigate(SettingsDirections.actionSettingsToEditContacts())
        }

        // Button to navigate to the Edit Contacts button
        binding.editMessagesBtn.setOnClickListener { view: View ->
            view.findNavController().navigate(SettingsDirections.actionSettingsToEditMessages())
        }

        binding.locationBtn.setOnClickListener {
            
        }

        return binding.root
    }


}
