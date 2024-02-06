package com.example.playlistmaker.domain.implemantation.player

import com.example.playlistmaker.domain.api.player.PlayerInteractor
import com.example.playlistmaker.domain.api.player.PlayerRepository

class PlayerInteractorImpl(private val playerRepository: PlayerRepository) : PlayerInteractor {

    override fun preparePlayer(previewUrl: String) {
        playerRepository.preparePlayer(previewUrl)
        playerRepository.prepareAsync()
    }

    override fun playerCheck(): Boolean {
        return playerRepository.playerCheck()
    }

    override fun prepareAsync() {
        playerRepository.prepareAsync()
    }

    override fun startPlayer() {
        playerRepository.startPlayer()
    }

    override fun pausePlayer() {
        playerRepository.pausePlayer()
    }

    override fun release() {
        playerRepository.release()
    }

    override fun reset() {
        playerRepository.reset()
    }

    override fun setOnPreparedListener(listener: (() -> Unit)?) {
        playerRepository.setOnPreparedListener(listener)
    }

    override fun setOnCompletionListener(listener: (() -> Unit)?) {
        playerRepository.setOnCompletionListener(listener)
    }

    override fun getCurrentPosition(): Int {
        return playerRepository.getCurrentPosition()
    }
}