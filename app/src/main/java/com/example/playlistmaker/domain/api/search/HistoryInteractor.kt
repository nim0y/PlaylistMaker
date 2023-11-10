package com.example.playlistmaker.domain.api.search

import com.example.playlistmaker.domain.models.search.Track

interface HistoryInteractor {
    fun add(newTrack: Track)
    fun clear()
    fun read(): List<Track>
}