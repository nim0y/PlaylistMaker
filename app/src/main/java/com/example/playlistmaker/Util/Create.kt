package com.example.playlistmaker.Util

import com.example.playlistmaker.player.data.impl.PlayerImpl
import com.example.playlistmaker.player.domain.PlayerInteractor

object Create {
    fun providePlayer(): PlayerInteractor {
        return PlayerInteractor(PlayerImpl())
    }
}