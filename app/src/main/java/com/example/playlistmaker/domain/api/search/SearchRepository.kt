package com.example.playlistmaker.domain.api.search


import com.example.playlistmaker.domain.SearchResult
import com.example.playlistmaker.domain.models.search.Track


interface SearchRepository {
    fun searchTrack(expression: String): SearchResult<List<Track>>
}