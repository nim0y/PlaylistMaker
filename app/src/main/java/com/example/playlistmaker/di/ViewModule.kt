package com.example.playlistmaker.di

import com.example.playlistmaker.ui.mediateka.favorite.FavoriteTracksViewModel
import com.example.playlistmaker.ui.mediateka.playlists.CurrentPlaylistViewModel
import com.example.playlistmaker.ui.mediateka.playlists.ModifyPlaylistViewModel
import com.example.playlistmaker.ui.mediateka.playlists.NewPlaylistViewModel
import com.example.playlistmaker.ui.mediateka.playlists.PlayListViewModel
import com.example.playlistmaker.ui.player.AudioPlayerViewModel
import com.example.playlistmaker.ui.search.SearchViewModel
import com.example.playlistmaker.ui.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(searchInteractor = get(), historyInteractor = get())
    }
    viewModel {
        SettingsViewModel(settingsInteractor = get(), externalNavigatorInteractor = get())
    }
    viewModel {
        AudioPlayerViewModel(
            playerInteractor = get(), favoriteTracksInteractor = get(), playlistsInteractor = get()
        )
    }
    viewModel {
        PlayListViewModel(get())
    }
    viewModel {
        NewPlaylistViewModel(get())
    }
    viewModel {
        FavoriteTracksViewModel(favoriteTracksInteractor = get())
    }
    viewModel {
        CurrentPlaylistViewModel(get())
    }
    viewModel {
        ModifyPlaylistViewModel(get())
    }
}