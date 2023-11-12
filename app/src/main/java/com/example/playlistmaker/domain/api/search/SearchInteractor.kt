package com.example.playlistmaker.domain.api.search

import com.example.playlistmaker.domain.models.search.Track

interface SearchInteractor {
    fun searchTracks(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(found: List<Track>?, errorId: String?)
    }
}