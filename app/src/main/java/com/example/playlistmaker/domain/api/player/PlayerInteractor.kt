package com.example.playlistmaker.domain.api.player

interface PlayerInteractor {
    fun preparePlayer(previewUrl: String?)

    fun playerCheck(): Boolean

    fun prepareAsync()

    fun startPlayer()

    fun pausePlayer()

    fun release()

    fun setOnPreparedListener(listener: (() -> Unit)?)

    fun setOnCompletionListener(listener: (() -> Unit)?)

    fun getCurrentPosition(): Int
}