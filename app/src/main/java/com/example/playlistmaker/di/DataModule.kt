package com.example.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Constants.IS_DARK_APP_THEME_KEY
import com.example.playlistmaker.data.dto.Constants.ITUNES_URL
import com.example.playlistmaker.data.dto.Constants.PREFER_SEARCH
import com.example.playlistmaker.data.impl.search.HistoryRepositoryImpl
import com.example.playlistmaker.data.impl.settings.SettingsRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.SearchApi
import com.example.playlistmaker.domain.implemantation.player.PlayerInteractorImpl
import com.example.playlistmaker.domain.implemantation.settings.ButtonsInteractorImpl
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<SearchApi> {
        Retrofit.Builder()
            .baseUrl(ITUNES_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchApi::class.java)
    }

    single { Gson() }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }
    single(named(PREFER_SEARCH)) {
        androidContext().getSharedPreferences(PREFER_SEARCH, Context.MODE_PRIVATE)
    }
    single {
        HistoryRepositoryImpl(get(), get())
    }
    factory {
        PlayerInteractorImpl(get())
    }
    single<SharedPreferences> {
        androidContext().getSharedPreferences(IS_DARK_APP_THEME_KEY, Context.MODE_PRIVATE)
    }
    factory {
        SettingsRepositoryImpl(get())
    }
    factory {
        ButtonsInteractorImpl(get())
    }

}