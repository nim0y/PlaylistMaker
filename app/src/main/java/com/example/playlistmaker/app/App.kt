package com.example.playlistmaker.app

import android.app.Application
import android.content.res.Configuration
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.domain.api.settings.AppTheme
import com.example.playlistmaker.domain.api.settings.SettingsInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    private var switchOn: Boolean = false


    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(androidContext = this@App)
            modules(dataModule, interactorModule, repositoryModule, viewModelModule)
        }

        val settingsInTouch: SettingsInteractor by inject()
        val isDeviceDarkMode =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        ThemeSwitch.switch(isDeviceDarkMode)
        if (isDeviceDarkMode) {
            switchOn = true
            settingsInTouch.setTheme(AppTheme(true))
        } else {
            switchOn = settingsInTouch.getTheme().switchIsOn
        }
        ThemeSwitch.switch(switchOn)
    }
}