package com.example.playlistmaker.Util

import com.example.playlistmaker.player.data.impl.PlayerImpl
import com.example.playlistmaker.player.domain.PlayerUseCaseInter

object Create {
    fun providePlayer(): PlayerUseCaseInter {
        return PlayerUseCaseInter(PlayerImpl())
    }
}