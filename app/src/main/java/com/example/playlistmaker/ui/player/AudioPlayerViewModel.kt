package com.example.playlistmaker.ui.player

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.api.player.PlayerInteractor
import com.example.playlistmaker.domain.models.search.player.PlayerState
import com.example.playlistmaker.utils.TIMER_DELAY


class AudioPlayerViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    private val _playerState = MutableLiveData<PlayerState>()
    private val _timerLiveData = MutableLiveData(0)
    private val _unifiedLiveData = MediatorLiveData<Pair<PlayerState, Int>>()
    val unifiedLiveData: LiveData<Pair<PlayerState, Int>> = _unifiedLiveData

    private var handler = Handler(Looper.getMainLooper())
    private val timerRunnable = object : Runnable {
        override fun run() {
            _timerLiveData.postValue(playerInteractor.getCurrentPosition())
            handler.postDelayed(this, TIMER_DELAY)
        }
    }

    private fun setState(playerState: PlayerState, timerState: Int) {
        _unifiedLiveData.postValue(Pair(playerState, timerState))
    }

    init {
        _unifiedLiveData.addSource(_playerState) { playerState ->
            _unifiedLiveData.value = Pair(playerState, _timerLiveData.value ?: 0)
        }
        _unifiedLiveData.addSource(_timerLiveData) { timerState ->
            _unifiedLiveData.value =
                Pair(_playerState.value ?: PlayerState.DEFAULT_STATE, timerState)
        }
    }

    fun onPause() {
        playerInteractor.pausePlayer()
        setState(PlayerState.PAUSE_STATE, _timerLiveData.value ?: 0)
        Log.e("myLog", "_______$")
        handler.removeCallbacks(timerRunnable)
    }

    fun onDestroy() {
        setState(PlayerState.DEFAULT_STATE, _timerLiveData.value ?: 0)
        playerInteractor.release()
        handler.removeCallbacks(timerRunnable)
        _unifiedLiveData.removeSource(_playerState)
        _unifiedLiveData.removeSource(_timerLiveData)
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
        _unifiedLiveData.removeSource(_playerState)
        _unifiedLiveData.removeSource(_timerLiveData)
    }

    fun setPlayer(trackPreviewUrl: String) {
        playerInteractor.preparePlayer(trackPreviewUrl)
        playerInteractor.prepareAsync()
        playerInteractor.setOnPreparedListener {
            setState(PlayerState.PREPARATION_STATE, _timerLiveData.value ?: 0)
        }
        playerInteractor.setOnCompletionListener {
            setState(PlayerState.PREPARATION_STATE, _timerLiveData.value ?: 0)
            handler.removeCallbacks(timerRunnable)
            _timerLiveData.postValue(0)
        }
    }

    fun playControl() {
        if (playerInteractor.playerCheck()) {
            playerInteractor.pausePlayer()
            setState(PlayerState.PAUSE_STATE, _timerLiveData.value ?: 0)
            handler.removeCallbacks(timerRunnable)
        } else {
            playerInteractor.startPlayer()
            setState(PlayerState.PLAYING_STATE, _timerLiveData.value ?: 0)
            handler.post(timerRunnable)
        }
    }
}