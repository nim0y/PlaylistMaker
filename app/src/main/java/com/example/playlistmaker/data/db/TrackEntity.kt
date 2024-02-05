package com.example.playlistmaker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "favorite_tracks_tablet")
data class TrackEntity(
    @PrimaryKey
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
    val insertTime: Long = Date().time
)