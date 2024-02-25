package com.example.playlistmaker.ui.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.player.PlayerInteractor
import com.example.playlistmaker.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.domain.db.PlaylistsInteractor
import com.example.playlistmaker.domain.models.search.Playlist
import com.example.playlistmaker.domain.models.search.Track
import com.example.playlistmaker.domain.models.search.player.AudioPlayerState
import com.example.playlistmaker.domain.models.search.player.FavoriteState
import com.example.playlistmaker.domain.models.search.player.PlayerState
import com.example.playlistmaker.domain.models.search.playlist.PlaylistState
import com.example.playlistmaker.utils.TIMER_DELAY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class AudioPlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favoriteTracksInteractor: FavoriteTracksInteractor,
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private val _favoriteState = MutableLiveData<FavoriteState>()
    val favoriteState: LiveData<FavoriteState> = _favoriteState

    private val _playlistsState = MutableStateFlow<PlaylistState>(PlaylistState.Empty)
    val playlistState: StateFlow<PlaylistState> = _playlistsState

    private val _audioPlayerState = MutableLiveData(
        AudioPlayerState(
            playerState = PlayerState.DEFAULT_STATE,
            timerValue = 0
        )
    )
    val audioPlayerState: LiveData<AudioPlayerState> = _audioPlayerState
    private var isFavoriteTrack: Boolean = false

    private var timerJob: Job? = null

    private fun setState(playerState: PlayerState) {
        _audioPlayerState.postValue(audioPlayerState.value?.copy(playerState = playerState))
    }

    private fun setTimer() {
        timerJob = viewModelScope.launch {
            while (playerInteractor.playerCheck()) {
                delay(TIMER_DELAY)
                _audioPlayerState.postValue(
                    _audioPlayerState.value?.copy(
                        timerValue = playerInteractor.getCurrentPosition()
                    )
                )
            }
        }
    }

    fun isFavorite(track: Track) {
        viewModelScope.launch {
            favoriteTracksInteractor
                .isFavoriteTrack(track.trackId ?: 0)
                .collect { isFavorite ->
                    isFavoriteTrack = isFavorite
                    _favoriteState.postValue(FavoriteState(isFavorite))

                }
        }
    }

    fun inPlaylist(playlist: Playlist, trackId: Long): Boolean {
        var data = false
        for (track in playlist.tracksIds) {
            if (track == trackId) data = true
        }
        return data
    }

    fun clickOnAddtoPlaylist(playlist: Playlist, track: Track) {
        viewModelScope.launch {
            playlist.tracksAmount = playlist.tracksIds.size + 1
            playlistsInteractor.addTrackToPlaylist(playlist, track)
        }
    }

    fun getPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.getPlaylists().collect() { playlists ->
                if (playlists.isEmpty()) {
                    _playlistsState.value = PlaylistState.Empty
                } else {
                    _playlistsState.value = PlaylistState.Data(playlists)
                }
            }
        }
    }

    fun clickOnFavorite(track: Track) {
        viewModelScope.launch {
            if (isFavoriteTrack) {
                favoriteTracksInteractor.deleteTrack(track.trackId ?: 0)
                _favoriteState.postValue(FavoriteState(false))
                isFavoriteTrack = false
            } else {
                favoriteTracksInteractor.insertTrack(track)
                _favoriteState.postValue(FavoriteState(true))
                isFavoriteTrack = true
            }
        }
    }

    fun onPause() {
        playerInteractor.pausePlayer()
        timerJob?.cancel()
        setState(playerState = PlayerState.PAUSE_STATE)
    }

    fun onDestroy() {
        setState(PlayerState.DEFAULT_STATE)
        timerJob?.cancel()
        playerInteractor.reset()
    }

    override fun onCleared() {
        super.onCleared()
        setState(PlayerState.DEFAULT_STATE)
        timerJob?.cancel()
        playerInteractor.reset()
    }

    fun setPlayer(trackPreviewUrl: String) {
        playerInteractor.preparePlayer(trackPreviewUrl)
        playerInteractor.setOnPreparedListener {
            setState(PlayerState.PREPARATION_STATE)
        }
        playerInteractor.setOnCompletionListener {
            timerJob?.cancel()
            _audioPlayerState.postValue(
                _audioPlayerState.value?.copy(
                    timerValue = 0,
                    playerState = PlayerState.PREPARATION_STATE
                )
            )
        }
    }

    fun playControl() {
        if (playerInteractor.playerCheck()) {
            playerInteractor.pausePlayer()
            setState(PlayerState.PAUSE_STATE)

        } else {
            playerInteractor.startPlayer()
            setState(PlayerState.PLAYING_STATE)
            setTimer()
        }
    }
}