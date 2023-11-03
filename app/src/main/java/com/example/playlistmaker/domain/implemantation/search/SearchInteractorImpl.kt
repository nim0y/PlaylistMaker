package com.example.playlistmaker.domain.implemantation.search


import com.example.playlistmaker.domain.SearchResult
import com.example.playlistmaker.domain.api.search.SearchInteractor
import com.example.playlistmaker.domain.api.search.SearchRepository
import java.util.concurrent.Executors


class SearchInteractorImpl(
    private val searchRepository: SearchRepository,
) : SearchInteractor {

    private val executor = Executors.newCachedThreadPool()
    override fun searchTracks(expression: String, consumer: SearchInteractor.TrackConsumer) {
        executor.execute {
            when (val res = searchRepository.searchTrack(expression)) {
                is SearchResult.Fail -> {
                    consumer.consume(null, res.errorId)
                }

                is SearchResult.Success -> {
                    consumer.consume(res.result, null)
                }
            }
        }
    }

}