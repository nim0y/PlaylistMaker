package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.models.search.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {
    suspend fun getTracks(): Flow<List<Track>>

    suspend fun insertTrack(track: Track)

    suspend fun deleteTrack(trackId: Int)

    suspend fun isFavoriteTrack(trackId: Int): Flow<Boolean>
}