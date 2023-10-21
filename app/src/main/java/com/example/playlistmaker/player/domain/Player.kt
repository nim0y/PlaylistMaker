package com.example.playlistmaker.player.domain

interface Player {

    fun preparePlayer(previewUrl: String)

    fun prepareAsync()

    fun playerCheck(): Boolean

    fun startPlayer()

    fun pausePlayer()

    fun release()

    fun setOnPreparedListener(listener: (() -> Unit)?)

    fun setOnCompletionListener(listener: (() -> Unit)?)

    fun getCurrentPosition(): Int

}