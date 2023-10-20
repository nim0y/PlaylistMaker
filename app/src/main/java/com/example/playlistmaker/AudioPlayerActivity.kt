package com.example.playlistmaker

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.Util.Create
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.domain.models.Track
import com.google.gson.Gson
import kotlinx.coroutines.Runnable
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private var binding: ActivityAudioPlayerBinding? = null
    private var track: Track? = null
    private var player = Create.providePlayer()
    private var state = PlayerState.DEFAULT_STATE
    private var playerTime: TextView? = null

    private val mainThreadHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        playerTime = findViewById(R.id.player_time)


        val audioPlayerHolder = AudioPlayerHolder(this)

        track = getTrack(intent.getStringExtra(CURRENT_TRACK))

        audioPlayerHolder.bind(track)

        setPlayer()

        binding?.buttonPlay?.setOnClickListener {
            playControl()
        }
        binding?.buttonBackPlayer?.setOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        player.pausePlayer()
        binding?.buttonPlay?.setImageResource(R.drawable.ic_play_button)
        state = PlayerState.PAUSE_STATE
        mainThreadHandler.removeCallbacks(timerUpdate())
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    private fun setPlayer() {
        player.preparePlayer(track!!.previewUrl)
        player.prepareAsync()
        player.setOnPreparedListener {
            state = PlayerState.PREPARATION_STATE
            binding?.buttonPlay?.setImageResource(R.drawable.ic_play_button)
        }
        player.setOnCompletionListener {
            binding?.buttonPlay?.setImageResource(R.drawable.ic_play_button)
            state = PlayerState.PREPARATION_STATE
            binding?.playerTime?.text = getString(R.string.def_time)
            mainThreadHandler.removeCallbacks(timerUpdate())
        }
    }

    private fun playControl() {
        when (state) {
            PlayerState.PLAYING_STATE -> {
                player.pausePlayer()
                binding?.buttonPlay?.setImageResource(R.drawable.ic_play_button)
                state = PlayerState.PAUSE_STATE
                mainThreadHandler.removeCallbacks(timerUpdate())

            }

            PlayerState.PREPARATION_STATE, PlayerState.PAUSE_STATE -> {
                player.startPlayer()
                binding?.buttonPlay?.setImageResource(R.drawable.ic_play_button_clicked)
                state = PlayerState.PLAYING_STATE
                mainThreadHandler.post(timerUpdate())
            }

            else -> Unit
        }
    }

    private fun timerUpdate(): Runnable {
        return object : Runnable {
            override fun run() {
                if (player.playerCheck()) {
                    binding?.playerTime?.text = SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(player.getCurrentPosition())
                    mainThreadHandler.postDelayed(this, 10L)
                }
            }
        }
    }

    private fun getTrack(json: String?) = Gson().fromJson(json, Track::class.java)
}