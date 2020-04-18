package com.example.getmeout.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.getmeout.R
import com.example.getmeout.view.SettingsDirections
import com.example.getmeout.databinding.FragmentSettingsBinding

/**
 * A simple [Fragment] subclass.
 */
class Settings : Fragment() {

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

        return binding.root
    }


}
