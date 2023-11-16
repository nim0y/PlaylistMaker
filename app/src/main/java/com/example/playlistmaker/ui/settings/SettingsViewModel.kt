package com.example.playlistmaker.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.app.ThemeSwitch
import com.example.playlistmaker.domain.api.settings.AppTheme
import com.example.playlistmaker.domain.api.settings.ExternalNavigatorInteractor
import com.example.playlistmaker.domain.api.settings.SettingsInteractor

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val externalNavigatorInteractor: ExternalNavigatorInteractor
) : ViewModel() {

    private val switchOnLiveData = MutableLiveData<Boolean>()
    val switchOnState: LiveData<Boolean> = switchOnLiveData
    private var isDarkThemeEnabled: Boolean = false

    init {
        isDarkThemeEnabled = settingsInteractor.getTheme().switchIsOn
        switchOnLiveData.value = isDarkThemeEnabled
    }

    fun onSwitchClicked(isChecked: Boolean) {
        switchOnLiveData.value = isChecked
        settingsInteractor.setTheme(AppTheme(switchIsOn = isChecked))
        ThemeSwitch.switch(isChecked)
    }

    fun onTermsOfUse() {
        externalNavigatorInteractor.termsOfUse()
    }

    fun onShare() {
        externalNavigatorInteractor.share()
    }

    fun onSupport() {
        externalNavigatorInteractor.support()
    }

}