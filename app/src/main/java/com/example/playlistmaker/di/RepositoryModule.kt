package com.example.playlistmaker.di

import com.example.playlistmaker.data.db.converters.TrackDbConverter
import com.example.playlistmaker.data.impl.db.FavoriteTracksRepositoryImpl
import com.example.playlistmaker.data.impl.player.PlayerRepositoryImpl
import com.example.playlistmaker.data.impl.search.HistoryRepositoryImpl
import com.example.playlistmaker.data.impl.search.SearchRepositoryImpl
import com.example.playlistmaker.data.impl.settings.ExternalNavigatorRepositoryImpl
import com.example.playlistmaker.data.impl.settings.SettingsRepositoryImpl
import com.example.playlistmaker.domain.api.player.PlayerRepository
import com.example.playlistmaker.domain.api.search.HistoryRepository
import com.example.playlistmaker.domain.api.search.SearchRepository
import com.example.playlistmaker.domain.api.settings.ExternalNavigatorRepository
import com.example.playlistmaker.domain.api.settings.SettingsRepository
import com.example.playlistmaker.domain.db.FavoriteTracksRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<SearchRepository> {
        SearchRepositoryImpl(networkClient = get())
    }
    factory<PlayerRepository> {
        PlayerRepositoryImpl(mediaPlayer = get())
    }

    factory { TrackDbConverter() }

    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(appDatabase = get(), trackDbConverter = get())
    }

    single<HistoryRepository> {
        HistoryRepositoryImpl(context = get(), gson = get())
    }
    single<SettingsRepository> {
        SettingsRepositoryImpl(sharedPreferences = get())
    }
    single<ExternalNavigatorRepository> {
        ExternalNavigatorRepositoryImpl(context = get())
    }

}