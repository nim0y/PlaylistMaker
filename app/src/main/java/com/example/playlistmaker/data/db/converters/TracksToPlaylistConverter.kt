package com.example.playlistmaker.data.db.converters

import com.example.playlistmaker.data.db.TrackToPlaylistEntity
import com.example.playlistmaker.domain.models.search.Track
import java.util.Date

class TracksToPlaylistConverter {
    fun map(track: Track): TrackToPlaylistEntity {
        return TrackToPlaylistEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            Date().time
        )
    }

    fun map(track: TrackToPlaylistEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            Date().time
        )
    }
}