package com.example.playlistmaker.ui.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.player.PlayerInteractor
import com.example.playlistmaker.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.domain.models.search.Track
import com.example.playlistmaker.domain.models.search.player.AudioPlayerState
import com.example.playlistmaker.domain.models.search.player.FavoriteState
import com.example.playlistmaker.domain.models.search.player.PlayerState
import com.example.playlistmaker.utils.TIMER_DELAY
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AudioPlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favoriteTracksInteractor: FavoriteTracksInteractor
) : ViewModel() {

    private val _favoriteState = MutableLiveData<FavoriteState>()
    val favoriteState: LiveData<FavoriteState> = _favoriteState

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
                .isFavoriteTrack(track.trackId.toInt())
                .collect { isFavorite ->
                    isFavoriteTrack = isFavorite
                    _favoriteState.postValue(FavoriteState(isFavorite))

                }
        }
    }

    fun clickOnFavorite(track: Track) {
        viewModelScope.launch {
            if (isFavoriteTrack) {
                favoriteTracksInteractor.deleteTrack(track.trackId.toInt())
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