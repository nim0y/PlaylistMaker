package com.example.playlistmaker.ui.player

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.data.dto.Constants.AP_ROUNDED_CORNERS
import com.example.playlistmaker.data.dto.Constants.PLAYER_TIME_TEMP
import com.example.playlistmaker.data.dto.Constants.SEARCH_QUERY_HISTORY
import com.example.playlistmaker.data.impl.player.PlayerRepositoryImpl
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.models.search.PlayerState
import com.example.playlistmaker.domain.models.search.Track
import kotlinx.coroutines.Runnable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private var binding: ActivityAudioPlayerBinding? = null
    val track by lazy {
        IntentCompat.getParcelableExtra(
            intent,
            SEARCH_QUERY_HISTORY,
            Track::class.java
        )
    }
    private var player = PlayerRepositoryImpl()
    private var state = PlayerState.DEFAULT_STATE
    private var playerTime: TextView? = null

    private val mainThreadHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        playerTime = findViewById(R.id.player_time)

        showTrack(track!!)

        setPlayer()

        binding?.buttonPlay?.setOnClickListener {
            playControl()
        }
        binding?.buttonBackPlayer?.setOnClickListener {
            finish()
        }
    }

    private fun showTrack(track: Track) {
        binding?.apply {
            trackName.text = track.trackName
            artistName.text = track.artistName
            trackGenreTv.text = track.primaryGenreName
            albumNameTv.text = track.collectionName
            trackCountryTv.text = track.country
            trackLengthTv.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
            trackYearTv.text = LocalDateTime.parse(
                track.releaseDate,
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            ).year.toString()

        }

        binding?.icPlayerDiscCover?.let {
            Glide.with(this)
                .load(track.artworkUrl512)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .transform(RoundedCorners(AP_ROUNDED_CORNERS))
                .into(it)
        }
        playerTime?.text = PLAYER_TIME_TEMP


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
        player.preparePlayer(track?.previewUrl)
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
}