package com.example.playlistmaker.domain.models.search

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Track(
    val trackId: String,
    var trackName: String,
    var artistName: String,
    var trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String,
    var releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    val insertTime: Long = Date().time,
    var isFavorite: Boolean = false
) : Parcelable {

    val artworkUrl512
        get() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
}




