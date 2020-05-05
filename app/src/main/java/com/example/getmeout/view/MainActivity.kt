package com.example.getmeout.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.getmeout.R
import com.example.getmeout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // We don't want to do anything in the Main Activity.
    // Rather, we'll go directly to a Title Fragment and handle things there.

    /*
    Note that this is part of a single activity - multiple fragment architecture.
    It's also know as the Fragment Navigation Pattern.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // We use databinding to automatically generate the classes required to bind the views
        // in the layout.
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,
            R.layout.activity_main
        )
    }
}
