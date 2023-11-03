package com.example.playlistmaker.ui.search

import com.example.playlistmaker.domain.models.search.Track

sealed interface State {
    object Load : State
    object Error : State
    class NothingFound() : State
    class SomeData(val tracks: List<Track>) : State
    class SomeHistory(val tracks: List<Track>) : State
}
