package com.example.playlistmaker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playlistmaker.utils.TRACKS_IN_PLAYLIST

@Entity(tableName = TRACKS_IN_PLAYLIST)
data class TrackToPlaylistEntity(
    @PrimaryKey(autoGenerate = true)
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
