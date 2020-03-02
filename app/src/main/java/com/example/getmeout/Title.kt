package com.example.getmeout


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.getmeout.databinding.FragmentTitleBinding

/**
 * A simple [Fragment] subclass.
 */
class Title : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentTitleBinding>(inflater,
            R.layout.fragment_title,container,false)
        // Inflate the layout for this fragment

        binding.settingsBtn.setOnClickListener { view: View ->
            view.findNavController().navigate(TitleDirections.actionTitleToSettings())
        }

        return binding.root

    }


}
