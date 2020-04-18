package com.example.getmeout.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.getmeout.R
import com.example.getmeout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // We don't want to do anything in the Main Activity.
    // Rather, we'll go directly to a Title Fragment and handle things there.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,
            R.layout.activity_main
        )
    }
}
