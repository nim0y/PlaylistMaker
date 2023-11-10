package com.example.playlistmaker.app

import android.app.Application
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule
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
        switchOn = settingsInTouch.getTheme().swichOn
        ThemeSwitch.switch(switchOn)
    }
}