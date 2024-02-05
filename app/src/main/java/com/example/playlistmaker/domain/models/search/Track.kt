package com.example.playlistmaker.domain.models.search

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Track(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    var insertTime: Long?
) : Parcelable {
    init {
        insertTime = insertTime ?: Date().time
    }

    val artworkUrl512
        get() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
}




