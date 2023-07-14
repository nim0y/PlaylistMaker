package com.example.playlistmaker

import android.app.Activity
import android.os.Bundle
import android.widget.Button


class SettingsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)
        val bButton=findViewById<Button>(R.id.bButton)
            bButton.setOnClickListener {
                finish()
            }
    }
}