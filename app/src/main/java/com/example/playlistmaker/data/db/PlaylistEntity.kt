package com.example.playlistmaker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playlistmaker.utils.PLAYLISTS_TABLET

@Entity(tableName = PLAYLISTS_TABLET)
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val description: String,
    val coverPath: String,
    val tracksIds: ArrayList<Long>,
    val tracksAmount: Int,
    val imageUri: String?
)