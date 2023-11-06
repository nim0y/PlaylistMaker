package com.example.playlistmaker.data.impl.player

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.player.PlayerRepository
import com.example.playlistmaker.domain.models.search.player.PlayerState

class PlayerRepositoryImpl : PlayerRepository {

    private var mediaPlayer = MediaPlayer()
    private var playerState = PlayerState.DEFAULT_STATE

    override fun preparePlayer(previewUrl: String?) {
        mediaPlayer.apply {
            setDataSource(previewUrl)

        }
    }

    override fun prepareAsync() {
        mediaPlayer.prepareAsync()
    }

    override fun playerCheck(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerState.PLAYING_STATE
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerState.PAUSE_STATE
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun setOnPreparedListener(listener: (() -> Unit)?) {
        mediaPlayer.setOnPreparedListener { listener?.invoke() }
    }

    override fun setOnCompletionListener(listener: (() -> Unit)?) {
        mediaPlayer.setOnCompletionListener { listener?.invoke() }
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }
}
