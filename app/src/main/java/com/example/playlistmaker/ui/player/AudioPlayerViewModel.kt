package com.example.playlistmaker.ui.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.player.PlayerInteractor
import com.example.playlistmaker.domain.models.search.player.AudioPlayerState
import com.example.playlistmaker.domain.models.search.player.PlayerState
import com.example.playlistmaker.utils.TIMER_DELAY
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AudioPlayerViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    private val _audioPlayerState = MutableLiveData(
        AudioPlayerState(
            playerState = PlayerState.DEFAULT_STATE,
            timerValue = 0
        )
    )
    val audioPlayerState: LiveData<AudioPlayerState> = _audioPlayerState

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

    fun setPlayer(trackPreviewUrl: String) {
        playerInteractor.preparePlayer(trackPreviewUrl)
        playerInteractor.prepareAsync()
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