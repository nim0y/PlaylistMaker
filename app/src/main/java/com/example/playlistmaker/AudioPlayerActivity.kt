package com.example.playlistmaker

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var playerTime: TextView
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

        audioPlayerHolder!!.bind(track)

        backButton.setOnClickListener {
            finish()
        }

        playPause.setOnClickListener {
            playPause.setImageResource(R.drawable.ic_play_button_clicked)
        }

        likeButton.setOnClickListener {
            likeButton.setImageResource(R.drawable.ic_like_button_clicked)
        }

        addToPlayList.setOnClickListener {
            addToPlayList.setImageResource(R.drawable.ic_add_button_clicked)
        }

    }

    private fun getTrack(json: String?) = Gson().fromJson(json, Track::class.java)

}