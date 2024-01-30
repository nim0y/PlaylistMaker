package com.example.playlistmaker.domain.api.search

import com.example.playlistmaker.domain.SearchResult
import com.example.playlistmaker.domain.models.search.Track
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun searchTrack(expression: String): Flow<SearchResult<List<Track>>>
}