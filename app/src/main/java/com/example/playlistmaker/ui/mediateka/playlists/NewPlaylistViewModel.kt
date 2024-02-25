package com.example.playlistmaker.ui.mediateka.playlists

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistsInteractor
import com.example.playlistmaker.domain.models.search.Playlist
import com.example.playlistmaker.domain.models.search.playlist.PlaylistState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class NewPlaylistViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    private val _playlistsState = MutableStateFlow<PlaylistState>(PlaylistState.Empty)
    val playlistState: StateFlow<PlaylistState> = _playlistsState

    private val _savedCoverUri = MutableLiveData<Uri?>()
    val savedCoverUri: LiveData<Uri?> = _savedCoverUri

    fun addPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistsInteractor.addPlaylist(playlist)
        }
    }

    fun saveCoverToStorage(previewUri: Uri, context: Context) {
        viewModelScope.launch {
            val savedUri = playlistsInteractor.saveCoverToPrivateStorage(previewUri, context)
            _savedCoverUri.postValue(savedUri)
        }
    }

    fun getCover(): String {
        return "cover_${UUID.randomUUID()}.jpg"
    }

}