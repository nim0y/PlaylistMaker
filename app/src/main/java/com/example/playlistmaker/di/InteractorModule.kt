package com.example.playlistmaker.di

import com.example.playlistmaker.domain.api.player.PlayerInteractor
import com.example.playlistmaker.domain.api.search.HistoryInteractor
import com.example.playlistmaker.domain.api.search.SearchInteractor
import com.example.playlistmaker.domain.api.settings.ButtonsInteractor
import com.example.playlistmaker.domain.api.settings.SettingsInteractor
import com.example.playlistmaker.domain.implemantation.player.PlayerInteractorImpl
import com.example.playlistmaker.domain.implemantation.search.HistoryInteractorImpl
import com.example.playlistmaker.domain.implemantation.search.SearchInteractorImpl
import com.example.playlistmaker.domain.implemantation.settings.ButtonsInteractorImpl
import com.example.playlistmaker.domain.implemantation.settings.SettingsInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    single<SearchInteractor> {
        SearchInteractorImpl(get())
    }
    factory<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }
    single<HistoryInteractor> {
        HistoryInteractorImpl(get())
    }
    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }
    single<ButtonsInteractor> {
        ButtonsInteractorImpl(get())
    }

}