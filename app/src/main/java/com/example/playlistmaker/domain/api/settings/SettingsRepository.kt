package com.example.playlistmaker.domain.api.settings

interface SettingsRepository {
    fun getTheme(): AppTheme
    fun setTheme(appTheme: AppTheme)
}