package com.example.playlistmaker.domain.implemantation.search

import com.example.playlistmaker.domain.api.search.HistoryInteractor
import com.example.playlistmaker.domain.api.search.HistoryRepository
import com.example.playlistmaker.domain.models.search.Track

class HistoryInteractorImpl(private val historyRepository: HistoryRepository) : HistoryInteractor {


    override fun add(newTrack: Track) {
        historyRepository.add(newTrack)
    }

    override fun clear() {
        historyRepository.clear()
    }

    override fun read(): List<Track> {
        return historyRepository.read()
    }
}