package com.example.playlistmaker.app

import androidx.appcompat.app.AppCompatDelegate

object ThemeSwitch {
    fun switch(switchIsOn: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (switchIsOn) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}