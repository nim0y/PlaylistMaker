package com.example.playlistmaker.ui.mediateka.playlists

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistsInteractor
import kotlinx.coroutines.launch

open class NewPlaylistViewModel(private val playlistsInteractor: PlaylistsInteractor) :
    ViewModel() {

//    private val _playlistsState = MutableStateFlow<PlaylistState>(PlaylistState.Empty)

    private val _savedCoverUri = MutableLiveData<Uri?>()
    val savedCoverUri: LiveData<Uri?> = _savedCoverUri

    fun newPlaylist(playlistName: String, playlistDescription: String, coverUri: Uri?) {
        viewModelScope.launch {
            playlistsInteractor.newPlaylist(playlistName, playlistDescription, coverUri)
        }
    }

    fun saveCoverToStorage(previewUri: Uri, context: Context) {
        viewModelScope.launch {
            val savedUri = playlistsInteractor.saveCoverToPrivateStorage(previewUri, context)
            _savedCoverUri.postValue(savedUri)
        }
    }
}