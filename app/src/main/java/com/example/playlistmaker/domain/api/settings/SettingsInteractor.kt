package com.example.playlistmaker.domain.api.settings

interface SettingsInteractor {
    fun getTheme(): AppTheme
    fun setTheme(appTheme: AppTheme)

}