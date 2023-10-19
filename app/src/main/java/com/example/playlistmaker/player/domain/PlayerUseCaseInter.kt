package com.example.playlistmaker.player.domain

class PlayerUseCaseInter(private val player: Player) {

    fun preparePlayer(previewUrl: String) {
        player.preparePlayer(previewUrl)
    }

    fun playerCheck(): Boolean {
        return player.playerCheck()
    }

    fun prepareAsync() {
        player.prepareAsync()
    }

    fun startPlayer() {
        player.startPlayer()
    }

    fun pausePlayer() {
        player.pausePlayer()
    }

    fun release() {
        player.release()
    }

    fun setOnPreparedListener(listener: (() -> Unit)?) {
        player.setOnPreparedListener(listener)
    }

    fun setOnCompletionListener(listener: (() -> Unit)?) {
        player.setOnCompletionListener(listener)
    }

    fun getCurrentPosition(): Int {
        return player.getCurrentPosition()
    }

}