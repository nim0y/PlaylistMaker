package com.example.playlistmaker.data.impl.search


import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.domain.SearchResult
import com.example.playlistmaker.domain.api.search.SearchRepository
import com.example.playlistmaker.domain.models.search.Track


class SearchRepositoryImpl(
    private val networkClient: NetworkClient
) : SearchRepository {


    override fun searchTrack(expression: String): SearchResult<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        when (response.resultCode) {
            200 -> {
                return SearchResult.Success((response as TracksSearchResponse).results.map {
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
                })
            }

            -1 -> {
                return SearchResult.Fail(errorId = "no connection")
            }

            else -> {
                return SearchResult.Fail(errorId = "nothing found")
            }

        }
    }


}

