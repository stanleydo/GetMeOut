package com.example.getmeout


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.getmeout.databinding.FragmentSettingsBinding
import com.example.getmeout.databinding.FragmentTitleBinding

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

        return binding.root
    }


}
