package com.example.getmeout.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.getmeout.R
import com.example.getmeout.databinding.FragmentEditMessagesBinding

// TODO - Add Recyclerview stuff

class EditMessages : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentEditMessagesBinding>(inflater,
            R.layout.fragment_edit_messages,container,false)
        // Inflate the layout for this fragment

        return binding.root
    }


}