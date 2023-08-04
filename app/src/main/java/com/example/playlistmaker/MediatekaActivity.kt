package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MediatekaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mediateka)

        val backButton = findViewById<Button>(R.id.buttonBack)

        backButton.setOnClickListener {
            finish()
        }
    }
}
