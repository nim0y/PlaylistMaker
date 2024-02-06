package com.example.playlistmaker.di

import com.example.playlistmaker.domain.api.player.PlayerInteractor
import com.example.playlistmaker.domain.api.search.HistoryInteractor
import com.example.playlistmaker.domain.api.search.SearchInteractor
import com.example.playlistmaker.domain.api.settings.ExternalNavigatorInteractor
import com.example.playlistmaker.domain.api.settings.SettingsInteractor
import com.example.playlistmaker.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.domain.implemantation.favorites.FavoriteTracksInteractorImpl
import com.example.playlistmaker.domain.implemantation.player.PlayerInteractorImpl
import com.example.playlistmaker.domain.implemantation.search.HistoryInteractorImpl
import com.example.playlistmaker.domain.implemantation.search.SearchInteractorImpl
import com.example.playlistmaker.domain.implemantation.settings.ExternalNavigatorInteractorImpl
import com.example.playlistmaker.domain.implemantation.settings.SettingsInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    single<SearchInteractor> {
        SearchInteractorImpl(searchRepository = get())
    }
    factory<PlayerInteractor> {
        PlayerInteractorImpl(playerRepository = get())
    }
    single<HistoryInteractor> {
        HistoryInteractorImpl(historyRepository = get())
    }
    single<SettingsInteractor> {
        SettingsInteractorImpl(settingsRepository = get())
    }
    single<ExternalNavigatorInteractor> {
        ExternalNavigatorInteractorImpl(externalNavigatorRepository = get())
    }
    single<FavoriteTracksInteractor> {
        FavoriteTracksInteractorImpl(favoriteTracksRepository = get())
    }

}