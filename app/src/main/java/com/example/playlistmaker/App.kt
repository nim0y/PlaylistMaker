package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    var switchOn: Boolean = false

    override fun onCreate() {
        super.onCreate()

        val sPref = getSharedPreferences(APP_THEME_SHARED_PREFERENCES, MODE_PRIVATE)
        switchOn = sPref.getBoolean(IS_DARK_APP_THEME_KEY, false)
        themeToggle(switchOn)
    }

    fun themeToggle(switchOn: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            when (switchOn) {
                true -> AppCompatDelegate.MODE_NIGHT_YES
                false -> AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}