package com.example.playlistmaker.ui.search

import com.example.playlistmaker.domain.models.search.Track

sealed interface State {
    data object Load : State
    data object Error : State
    data object NothingFound : State
    class SomeData(val tracks: List<Track>) : State
    class SomeHistory(val tracks: List<Track>) : State
}
