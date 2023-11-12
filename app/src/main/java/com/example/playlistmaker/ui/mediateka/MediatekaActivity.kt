package com.example.playlistmaker.ui.mediateka

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivityMediatekaBinding

class MediatekaActivity : AppCompatActivity() {

    private var binding: ActivityMediatekaBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMediatekaBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.buttonBack?.setOnClickListener {
            finish()
        }
    }
}
