package com.example.playlistmaker.ui.player

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.models.search.Track
import com.example.playlistmaker.domain.models.search.player.PlayerState
import com.example.playlistmaker.utils.SEARCH_QUERY_HISTORY
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private var binding: ActivityAudioPlayerBinding? = null
    private lateinit var track: Track
    private val vm by viewModel<AudioPlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        track = IntentCompat.getParcelableExtra(intent, SEARCH_QUERY_HISTORY, Track::class.java)!!

        showTrack(track)

        vm.setPlayer(track.previewUrl!!)

        vm.audioPlayerState.observe(this) {
            execute(it)
        }

        vm.timer.observe(this) {
            binding?.playerTime?.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(it)
        }

        binding?.buttonPlay?.setOnClickListener {
            vm.playControl()
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
                .transform(RoundedCorners(applicationContext.resources.getDimensionPixelSize(R.dimen.track_corner_radious)))
                .into(it)
        }
    }

    private fun execute(playerState: PlayerState) {
        when (playerState) {
            PlayerState.PLAYING_STATE -> setIconPause()
            PlayerState.PAUSE_STATE -> setIconPlay()
            PlayerState.PREPARATION_STATE -> setIconPlay()
            PlayerState.DEFAULT_STATE -> {}
        }
    }

    override fun onPause() {
        vm.onPause()
        binding?.buttonPlay?.setImageResource(R.drawable.ic_play_button)
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.onDestroy()
    }

    private fun setIconPlay() {
        binding?.buttonPlay?.setImageResource(R.drawable.ic_play_button)
    }

    private fun setIconPause() {
        binding?.buttonPlay?.setImageResource(R.drawable.ic_play_button_clicked)
    }
}