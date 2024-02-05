package com.example.playlistmaker.data.impl.db

import com.example.playlistmaker.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.domain.db.FavoriteTracksRepository
import com.example.playlistmaker.domain.models.search.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(private val favoriteTracksRepository: FavoriteTracksRepository) :
    FavoriteTracksInteractor {
    override suspend fun getTracks(): Flow<List<Track>> {
        return favoriteTracksRepository.getTracks()
    }

    override suspend fun insertTrack(track: Track) {
        return favoriteTracksRepository.insertTrack(track)
    }

    override suspend fun deleteTrack(trackId: Int) {
        return favoriteTracksRepository.deleteTrack(trackId = trackId)
    }

    override suspend fun isFavoriteTrack(trackId: Int): Flow<Boolean> {
        return favoriteTracksRepository.isFavoriteTrack(trackId)
    }
}