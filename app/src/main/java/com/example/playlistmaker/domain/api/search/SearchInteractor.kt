package com.example.playlistmaker.domain.api.search

import com.example.playlistmaker.domain.models.search.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    suspend fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>>
}