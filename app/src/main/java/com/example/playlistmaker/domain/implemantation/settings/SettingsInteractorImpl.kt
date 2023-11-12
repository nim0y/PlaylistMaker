package com.example.playlistmaker.domain.implemantation.settings

import com.example.playlistmaker.domain.api.settings.AppTheme
import com.example.playlistmaker.domain.api.settings.SettingsInteractor
import com.example.playlistmaker.domain.api.settings.SettingsRepository

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) :
    SettingsInteractor {
    override fun getTheme(): AppTheme =
        settingsRepository.getTheme()

    override fun setTheme(appTheme: AppTheme) {
        settingsRepository.setTheme(appTheme)
    }

}