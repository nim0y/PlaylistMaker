package com.example.playlistmaker.ui.mediateka.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.FavoriteTracksInteractor
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val favoriteTracksInteractor: FavoriteTracksInteractor
) : ViewModel() {

    private val _favoriteState = MutableLiveData<FavoriteState>()
    val favoriteState: LiveData<FavoriteState> = _favoriteState

    private fun setState(favoriteState: FavoriteState) {
        _favoriteState.postValue(favoriteState)
    }

    init {
        setState(FavoriteState.Load)
    }

    fun getFavoriteList() {
        viewModelScope.launch {
            setState(FavoriteState.Load)
            favoriteTracksInteractor
                .getTracks()
                .collect { tracks ->
                    if (tracks.isEmpty()) {
                        setState(FavoriteState.NoEntry)
                    } else {
                        setState(FavoriteState.Content(tracks))
                    }
                }
        }
    }

}