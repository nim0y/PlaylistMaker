package com.example.playlistmaker.domain.implemantation.search


import com.example.playlistmaker.domain.SearchResult
import com.example.playlistmaker.domain.api.search.SearchInteractor
import com.example.playlistmaker.domain.api.search.SearchRepository
import com.example.playlistmaker.domain.models.search.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SearchInteractorImpl(
    private val searchRepository: SearchRepository,
) : SearchInteractor {
    override suspend fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>> {
        return searchRepository.searchTrack(expression).map { res ->
            when (res) {
                is SearchResult.Fail -> null to res.errorId
                is SearchResult.Success -> res.result to null
            }
        }
    }

}