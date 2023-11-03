package com.example.playlistmaker.domain.models.search

import android.icu.text.SimpleDateFormat
import android.os.Parcel
import android.os.Parcelable
import java.util.Locale

data class Track(
    val trackId: String?,
    var trackName: String?,
    var artistName: String?,
    var trackTimeMillis: Long?,
    val artworkUrl100: String?,
    val collectionName: String?,
    var releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?
) : Parcelable {

    val artworkUrl512
        get() = artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

    val trackTime
        get() = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(trackTimeMillis?.toLong())

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(trackId)
        parcel.writeString(trackName)
        parcel.writeString(artistName)
        trackTimeMillis?.let { parcel.writeLong(it) }
        parcel.writeString(artworkUrl100)
        parcel.writeString(collectionName)
        parcel.writeString(releaseDate)
        parcel.writeString(primaryGenreName)
        parcel.writeString(country)
        parcel.writeString(previewUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Track> {
        override fun createFromParcel(parcel: Parcel): Track {
            return Track(parcel)
        }

        override fun newArray(size: Int): Array<Track?> {
            return arrayOfNulls(size)
        }
    }
}




