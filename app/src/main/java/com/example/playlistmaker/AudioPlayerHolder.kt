package com.example.playlistmaker

import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.data.Track
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

const val AP_ROUNDED_CORNERS = 15
const val PLAYER_TIME_TEMP = "00:00"

class AudioPlayerHolder(private val audioPlayerHolder: AudioPlayerActivity) {
    private val trackCover = audioPlayerHolder.findViewById<ImageView>(R.id.ic_player_disc_cover)
    private val trackName = audioPlayerHolder.findViewById<TextView>(R.id.track_name)
    private val artistName = audioPlayerHolder.findViewById<TextView>(R.id.artist_name)
    private val trackLenght = audioPlayerHolder.findViewById<TextView>(R.id.track_length_tv)
    private val trackGenre = audioPlayerHolder.findViewById<TextView>(R.id.track_genre_tv)
    private val playerTime = audioPlayerHolder.findViewById<TextView>(R.id.player_time)
    private val trackCountry = audioPlayerHolder.findViewById<TextView>(R.id.track_country_tv)
    private val trackYear = audioPlayerHolder.findViewById<TextView>(R.id.track_year_tv)
    private val albumName = audioPlayerHolder.findViewById<TextView>(R.id.album_name_tv)

    fun bind(item: Track?) {
        trackName.text = item?.trackName
        artistName.text = item?.artistName
        trackGenre.text = item?.primaryGenreName
        albumName.text = item?.collectionName
        trackCountry.text = item?.country
        playerTime.text = PLAYER_TIME_TEMP
        trackLenght.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(item?.trackTimeMillis)
        trackYear.text = LocalDateTime.parse(
            item?.releaseDate,
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        ).year.toString()

        Glide.with(audioPlayerHolder)
            .load(item?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.ic_placeholder)
            .centerCrop()
            .transform(RoundedCorners(AP_ROUNDED_CORNERS))
            .into(trackCover)
    }
}