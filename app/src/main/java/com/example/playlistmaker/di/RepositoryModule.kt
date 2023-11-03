package com.example.playlistmaker.di

import com.example.playlistmaker.data.impl.player.PlayerRepositoryImpl
import com.example.playlistmaker.data.impl.search.HistoryRepositoryImpl
import com.example.playlistmaker.data.impl.search.SearchRepositoryImpl
import com.example.playlistmaker.data.impl.settings.SettingsRepositoryImpl
import com.example.playlistmaker.domain.api.player.PlayerRepository
import com.example.playlistmaker.domain.api.search.HistoryRepository
import com.example.playlistmaker.domain.api.search.SearchRepository
import com.example.playlistmaker.domain.api.settings.ButtonsRepository
import com.example.playlistmaker.domain.api.settings.SettingsRepository
import com.example.playlistmaker.domain.implemantation.settings.ButtonsRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<SearchRepository> {
        SearchRepositoryImpl(get())
    }
    factory<PlayerRepository> {
        PlayerRepositoryImpl()
    }
    single<HistoryRepository> {
        HistoryRepositoryImpl(get(), get())
    }
    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }
    single<ButtonsRepository> {
        ButtonsRepositoryImpl(get())
    }

}