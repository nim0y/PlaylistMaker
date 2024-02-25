package com.example.playlistmaker.data.impl.db

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.example.playlistmaker.data.db.AppDatabasePlaylists
import com.example.playlistmaker.data.db.PlaylistEntity
import com.example.playlistmaker.data.db.converters.PlaylistsDbConverter
import com.example.playlistmaker.data.db.converters.TracksToPlaylistConverter
import com.example.playlistmaker.domain.db.PlaylistsRepository
import com.example.playlistmaker.domain.models.search.Playlist
import com.example.playlistmaker.domain.models.search.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream

class PlaylistsRepositoryImpl(
    private val appDatabasePlaylists: AppDatabasePlaylists,
    private val playlistsDbConverter: PlaylistsDbConverter,
    private val tracksToPlaylistConverter: TracksToPlaylistConverter
) : PlaylistsRepository {
    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabasePlaylists.playlistsDao().getAllPlayLists()
        emit(converterForEntity(playlists))
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabasePlaylists.playlistsDao().insertPlayList(playlistsDbConverter.map(playlist))
    }

    override suspend fun addTrackToPlaylist(playList: Playlist, track: Track) {
        playList.tracksIds.add(track.trackId?.toLong() ?: 0)
        addPlaylist(playlist = playList)
        appDatabasePlaylists.playlistsDao().addTrackToPlaylist(tracksToPlaylistConverter.map(track))
    }

    override suspend fun saveCoverToPrivateStorage(previewUri: Uri, context: Context): Uri? {
        val path =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myPics")
        if (!path.exists()) {
            path.mkdirs()
        }
        val file = File(path, "playlist_cover_${System.currentTimeMillis()}.jpg")
        val input = context.contentResolver.openInputStream(previewUri)
        val output = FileOutputStream(file)
        BitmapFactory
            .decodeStream(input)
            .compress(Bitmap.CompressFormat.JPEG, 30, output)
        val previewUri = Uri.fromFile(file)
        return previewUri
    }

    override suspend fun getPlaylistById(playlistId: Int): Playlist {
        return converterForPlaylistEntity(
            appDatabasePlaylists.playlistsDao().getPlaylistById(playlistId)
        )
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        val playlist = getPlaylistById(playlistId)
        appDatabasePlaylists.playlistsDao().deletePlaylist(playlistsDbConverter.map(playlist))
    }

    private fun converterForEntity(playlist: List<PlaylistEntity>): List<Playlist> {
        return playlist.map { playlist -> playlistsDbConverter.map(playlist) }
    }

    private fun converterForPlaylistEntity(playlist: PlaylistEntity): Playlist {
        return playlistsDbConverter.map(playlist)
    }
}