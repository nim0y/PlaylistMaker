package com.example.playlistmaker.ui.player

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.api.player.PlayerInteractor
import com.example.playlistmaker.domain.models.search.player.AudioPlayerState
import com.example.playlistmaker.domain.models.search.player.PlayerState
import com.example.playlistmaker.utils.TIMER_DELAY


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

    private var handler = Handler(Looper.getMainLooper())
    private val timerRunnable = object : Runnable {
        override fun run() {
            _audioPlayerState.postValue(
                _audioPlayerState.value?.copy(
                    timerValue = playerInteractor.getCurrentPosition()
                )
            )
            handler.postDelayed(this, TIMER_DELAY)
        }
    }

    private fun setState(playerState: PlayerState) {
        _audioPlayerState.postValue(audioPlayerState.value?.copy(playerState = playerState))
    }

    private fun setTimer() {
        _audioPlayerState.postValue(
            audioPlayerState.value?.copy(
                playerState = PlayerState.PREPARATION_STATE,
                timerValue = 0
            )
        )
    }

    fun onPause() {
        playerInteractor.pausePlayer()
        setState(playerState = PlayerState.PAUSE_STATE)
        handler.removeCallbacks(timerRunnable)
    }

    fun onDestroy() {
        setState(PlayerState.DEFAULT_STATE)
        playerInteractor.release()
        handler.removeCallbacks(timerRunnable)
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
    }

    fun setPlayer(trackPreviewUrl: String) {
        playerInteractor.preparePlayer(trackPreviewUrl)
        playerInteractor.prepareAsync()
        playerInteractor.setOnPreparedListener {
            setState(PlayerState.PREPARATION_STATE)
        }
        playerInteractor.setOnCompletionListener {
            handler.removeCallbacks(timerRunnable)
            setTimer()
        }
    }

    fun playControl() {
        if (playerInteractor.playerCheck()) {
            playerInteractor.pausePlayer()
            setState(PlayerState.PAUSE_STATE)
            handler.removeCallbacks(timerRunnable)
        } else {
            playerInteractor.startPlayer()
            setState(PlayerState.PLAYING_STATE)
            handler.post(timerRunnable)
        }
    }
}