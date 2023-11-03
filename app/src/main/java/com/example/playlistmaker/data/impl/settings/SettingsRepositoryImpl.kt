package com.example.playlistmaker.data.impl.settings

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.data.dto.Constants.IS_DARK_APP_THEME_KEY
import com.example.playlistmaker.domain.api.settings.AppTheme
import com.example.playlistmaker.domain.api.settings.SettingsRepository


class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    SettingsRepository {
    override fun getTheme(): AppTheme =
        AppTheme(sharedPreferences.getBoolean(IS_DARK_APP_THEME_KEY, false))

    override fun setTheme(appTheme: AppTheme) {
        sharedPreferences.edit {
            putBoolean(IS_DARK_APP_THEME_KEY, appTheme.swichOn)
        }
    }

}