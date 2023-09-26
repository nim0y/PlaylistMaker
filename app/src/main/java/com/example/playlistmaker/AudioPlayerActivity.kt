package com.example.playlistmaker

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson


class AudioPlayerActivity : AppCompatActivity() {
    private var playerTime: TextView? = null
    private lateinit var track: Track
    private var audioPlayerHolder: AudioPlayerHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        playerTime = findViewById(R.id.player_time)
        val backButton: Button = findViewById(R.id.buttonBackPlayer)
        val addToPlayList: ImageView = findViewById(R.id.button_add_track)
        val playPause: ImageView = findViewById(R.id.button_play)
        val likeButton: ImageView = findViewById(R.id.button_like)

        audioPlayerHolder = AudioPlayerHolder(this)

        track = getTrack(intent.getStringExtra(CURRENT_TRACK))
        Log.e("myLog", "$track")

        audioPlayerHolder!!.bind(track)

        backButton.setOnClickListener {
            finish()
        }

        playPause.setOnClickListener {
            Toast.makeText(this, "Кнопка Play нажата", Toast.LENGTH_SHORT).show()
        }

        likeButton.setOnClickListener {
            Toast.makeText(this, "${track.trackName} добавлена в favorite", Toast.LENGTH_SHORT)
                .show()
        }

        addToPlayList.setOnClickListener {
            Toast.makeText(this, "${track.trackName} Добавлен в Плейлист", Toast.LENGTH_SHORT)
                .show()
        }

    }

    private fun getTrack(json: String?) = Gson().fromJson(json, Track::class.java)

}