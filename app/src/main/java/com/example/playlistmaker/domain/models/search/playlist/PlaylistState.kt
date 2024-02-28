package com.example.playlistmaker.domain.models.search.playlist

import com.example.playlistmaker.domain.models.search.Playlist

sealed interface PlaylistState {
    data class Data(val tracks: List<Playlist>) : PlaylistState

    data object Empty : PlaylistState

    data object Load : PlaylistState
}