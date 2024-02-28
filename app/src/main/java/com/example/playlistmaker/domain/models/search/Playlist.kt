package com.example.playlistmaker.domain.models.search

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val id: Int,
    val name: String,
    val description: String,
    val coverPath: String,
    val tracksIds: ArrayList<Long>,
    var tracksAmount: Int,
    val imageUri: String?
) : Parcelable
