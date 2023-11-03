package com.example.playlistmaker.ui.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.app.ThemeSwitch
import com.example.playlistmaker.domain.api.settings.AppTheme
import com.example.playlistmaker.domain.api.settings.ButtonsInteractor
import com.example.playlistmaker.domain.api.settings.SettingsInteractor

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val buttonsInteractor: ButtonsInteractor
) : ViewModel() {

    private val switchOnLiveData = MutableLiveData<Boolean>()
    val switchOnState: LiveData<Boolean> = switchOnLiveData
    private var darkTheme = false


    init {
        darkTheme = settingsInteractor.getTheme().swichOn
        Log.e("myLog", "$darkTheme")
        switchOnLiveData.value = false
    }

    fun onSwitchClicked(isChecked: Boolean) {
        switchOnLiveData.value = isChecked
        settingsInteractor.setTheme(AppTheme(swichOn = isChecked))
        ThemeSwitch.switch(isChecked)
        Log.e("myLog", "$darkTheme")
    }

    fun onTermsOfUse() {
        buttonsInteractor.termsOfUse()
    }

    fun onShare() {
        buttonsInteractor.share()
    }

    fun onSupport() {
        buttonsInteractor.support()
    }

}