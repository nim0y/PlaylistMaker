package com.example.playlistmaker.domain.db

import android.content.Context
import android.net.Uri
import com.example.playlistmaker.domain.models.search.Playlist
import com.example.playlistmaker.domain.models.search.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {

    suspend fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun addTrackToPlaylist(playList: Playlist, track: Track)

    suspend fun saveCoverToPrivateStorage(previewUri: Uri, context: Context): Uri?

    suspend fun getPlaylistById(playlistId: Int): Playlist

    suspend fun deletePlaylist(playlistId: Int)

}