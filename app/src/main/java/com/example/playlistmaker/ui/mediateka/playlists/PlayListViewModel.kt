package com.example.playlistmaker.ui.mediateka.playlists

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistsInteractor
import com.example.playlistmaker.domain.models.search.playlist.PlaylistState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayListViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    private val _playlistsState = MutableLiveData<PlaylistState>(PlaylistState.Load)
    val playlistState: MutableLiveData<PlaylistState> = _playlistsState

    private fun setState(playlistState: PlaylistState) {
        _playlistsState.postValue(playlistState)
    }

    fun getPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor
                .getPlaylists()
                .collect { playlists ->
                    if (playlists.isEmpty()) {
                        setState(PlaylistState.Empty)
                    } else {
                        setState(PlaylistState.Data(playlists))
                    }
                }
        }
    }
}