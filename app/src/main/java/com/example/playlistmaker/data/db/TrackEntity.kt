package com.example.playlistmaker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playlistmaker.utils.FAVORITE_TRACKS_TABLET


@Entity(tableName = FAVORITE_TRACKS_TABLET)
data class TrackEntity(
    @PrimaryKey
    val trackId: Int?,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Long?,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
    var insertTime: Long?
)