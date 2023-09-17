package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    var switchOn: Boolean = false

    override fun onCreate() {
        super.onCreate()

        val sPref = getSharedPreferences(D_T_ON, MODE_PRIVATE)
        switchOn = sPref.getBoolean(S_P_STAT, false)
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