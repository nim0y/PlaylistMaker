package com.example.playlistmaker.ui.player

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.api.player.PlayerInteractor
import com.example.playlistmaker.domain.models.search.player.AudioPlayerState
import com.example.playlistmaker.domain.models.search.player.PlayerState
import com.example.playlistmaker.utils.TIMER_DELAY


class AudioPlayerViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    private val audioPlayerState = MutableLiveData<AudioPlayerState>()
    private val playerState = MutableLiveData<PlayerState>()
    val timerState = MutableLiveData<Int>()
    val mediatorLiveData = MediatorLiveData<PlayerState>().apply {
        addSource(playerState) { value ->
            value?.let {
                val currentPlayerState = audioPlayerState.value ?: AudioPlayerState(it, 0)
                audioPlayerState.value = currentPlayerState
            }
        }
        addSource(timerState) { value ->
            value?.let {
                val currentPlayerState =
                    audioPlayerState.value ?: AudioPlayerState(PlayerState.DEFAULT_STATE, it)
                audioPlayerState.value = currentPlayerState.copy(timerValue = it)
            }
        }
    }

    private var handler = Handler(Looper.getMainLooper())
    private val timerRunnable = object : Runnable {
        override fun run() {
            timerState.postValue(playerInteractor.getCurrentPosition())
            handler.postDelayed(this, TIMER_DELAY)
        }
    }

    private fun setState(playerState: PlayerState) {
        mediatorLiveData.postValue(playerState)
    }

    init {
        setState(PlayerState.DEFAULT_STATE)
        timerState.postValue(0)
    }

    fun onPause() {
        playerInteractor.pausePlayer()
        setState(PlayerState.PAUSE_STATE)
        Log.e("myLog", "_______$playerState")
        handler.removeCallbacks(timerRunnable)
    }

    fun onDestroy() {
        setState(PlayerState.DEFAULT_STATE)
        playerInteractor.release()
        handler.removeCallbacks(timerRunnable)
        mediatorLiveData.removeSource(audioPlayerState)
        mediatorLiveData.removeSource(timerState)
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
        mediatorLiveData.removeSource(audioPlayerState)
        mediatorLiveData.removeSource(timerState)
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
            timerState.postValue(0)
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