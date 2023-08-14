package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(trackView: View) : RecyclerView.ViewHolder(trackView) {
    private val trackCover = trackView.findViewById<ImageView>(R.id.ic_disc_cover)
    private val trackName = trackView.findViewById<TextView>(R.id.track_name_text_view)
    private val artistName = trackView.findViewById<TextView>(R.id.artist_name_text_view)
    private val trackLenght = trackView.findViewById<TextView>(R.id.track_lenght_text_view)

    fun bind(item: Track) {
        trackName.text = item.trackName
        artistName.text = item.artistName
        trackLenght.text = item.trackTime

        Glide.with(itemView.context)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.ic_placeholder)
            .fitCenter()
            .transform(RoundedCorners(5))
            .into(trackCover)
    }
}