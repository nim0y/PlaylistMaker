package com.example.playlistmaker.domain.api.player

interface PlayerRepository {

    fun preparePlayer(previewUrl: String)

    fun prepareAsync()

    fun playerCheck(): Boolean

    fun startPlayer()

    fun pausePlayer()

    fun release()

    fun reset()

    fun setOnPreparedListener(listener: (() -> Unit)?)

    fun setOnCompletionListener(listener: (() -> Unit)?)

    fun getCurrentPosition(): Int

}