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
import com.example.playlistmaker.utils.ILLEGAL_ARGUMENT_TRACK_ID
import com.example.playlistmaker.utils.NULL_ARGUMENT_TRACK_ID
import com.example.playlistmaker.utils.STORAGE_DIR_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream
import java.util.Date
import java.util.UUID

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
        val trackId = track.trackId?.toLong() ?: throw IllegalArgumentException(
            ILLEGAL_ARGUMENT_TRACK_ID
        )
        if (trackId == 0L) {
            throw IllegalArgumentException(NULL_ARGUMENT_TRACK_ID)
        }
        addPlaylist(playlist = playList)
        val addTime = Date().time
        appDatabasePlaylists.playlistsDao()
            .addTrackToPlaylist(tracksToPlaylistConverter.map(track, addTime))
    }

    override suspend fun saveCoverToPrivateStorage(previewUri: Uri, context: Context): Uri? {
        val path =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), STORAGE_DIR_NAME)
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

    override suspend fun newPlaylist(
        playlistName: String,
        playlistDescription: String,
        coverUri: Uri?
    ) {
        val coverPath = getCover()
        val playlist = Playlist(
            id = 0,
            name = playlistName,
            description = playlistDescription,
            coverPath = coverPath,
            tracksIds = arrayListOf(),
            tracksAmount = 0,
            imageUri = coverUri?.toString() ?: ""
        )
        addPlaylist(playlist)
    }

    override suspend fun getCover(): String {
        return "cover_${UUID.randomUUID()}.jpg"
    }

    private fun converterForEntity(playlist: List<PlaylistEntity>): List<Playlist> {
        return playlist.map { playlist -> playlistsDbConverter.map(playlist) }
    }

    private fun converterForPlaylistEntity(playlist: PlaylistEntity): Playlist {
        return playlistsDbConverter.map(playlist)
    }
}