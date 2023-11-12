package com.example.playlistmaker.ui.player

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.api.player.PlayerInteractor
import com.example.playlistmaker.domain.models.search.player.PlayerState
import com.example.playlistmaker.utils.TIMER_DELAY


class AudioPlayerViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    private val _audioPlayerState = MutableLiveData<PlayerState>()
    val audioPlayerState: LiveData<PlayerState> = _audioPlayerState
    private val _timerLiveData = MutableLiveData(0)
    val timer: LiveData<Int> = _timerLiveData

    private var handler = Handler(Looper.getMainLooper())
    private val timerRunnable = object : Runnable {
        override fun run() {
            _timerLiveData.postValue(playerInteractor.getCurrentPosition())
            handler.postDelayed(this, TIMER_DELAY)
        }
    }

    private fun setState(playerState: PlayerState) {
        _audioPlayerState.postValue(playerState)
    }

    init {
        setState(PlayerState.DEFAULT_STATE)
    }

    fun onPause() {
        playerInteractor.pausePlayer()
        setState(PlayerState.PAUSE_STATE)
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
            setState(PlayerState.PREPARATION_STATE)
            handler.removeCallbacks(timerRunnable)
            _timerLiveData.postValue(0)
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