package com.example.playlistmaker.ui.search

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ViewTrackBinding
import com.example.playlistmaker.domain.models.search.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(trackView: View) : RecyclerView.ViewHolder(trackView) {
    private val binding = ViewTrackBinding.bind(trackView)

    fun bind(item: Track) = with(binding) {
        trackNameTextView.text = item.trackName
        artistNameTextView.text = item.artistName
        trackLenghtTextView.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)

        Glide.with(itemView.context)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.ic_placeholder)
            .fitCenter()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.track_corner_radious)))
            .into(icDiscCover)
    }
}