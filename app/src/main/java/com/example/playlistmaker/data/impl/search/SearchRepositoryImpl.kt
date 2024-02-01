package com.example.playlistmaker.data.impl.search

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.domain.SearchResult
import com.example.playlistmaker.domain.api.search.SearchRepository
import com.example.playlistmaker.domain.models.search.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(
    private val networkClient: NetworkClient
) : SearchRepository {

    override suspend fun searchTrack(expression: String): Flow<SearchResult<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        when (response.resultCode) {
            200 -> {
                with(response as TracksSearchResponse) {
                    val data = results.map {
                        Track(
                            it.trackId,
                            it.trackName,
                            it.artistName,
                            it.trackTimeMillis,
                            it.artworkUrl100,
                            it.collectionName,
                            it.releaseDate,
                            it.primaryGenreName,
                            it.country,
                            it.previewUrl
                        )
                    }
                    emit(SearchResult.Success(data))
                }
            }

            -1 -> {
                emit(SearchResult.Fail(errorId = "no connection"))
            }

            else -> {
                emit(SearchResult.Fail(errorId = "nothing found"))
            }
        }
    }
}