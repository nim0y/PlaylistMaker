package com.example.playlistmaker.ui.mediateka.favorite

import com.example.playlistmaker.domain.models.search.Track

sealed interface FavoriteState {

    data class Content(val tracks: List<Track>) : FavoriteState

    data object NoEntry : FavoriteState

    data object Load : FavoriteState
}