package com.example.playlistmaker.ui.mediateka.playlists

import android.content.Context
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ViewPlaylistBinding
import com.example.playlistmaker.domain.models.search.Playlist

class PlaylistsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ViewPlaylistBinding.bind(view)

    fun bind(model: Playlist, clickListener: ClickListener) {
        binding.namePlaylist.text = model.name
        binding.descriptionPlaylist.text = setTracksAmount(itemView.context, model.tracksAmount)
        val previewUri = model.imageUri.let { Uri.parse(it) }

        Glide.with(itemView)
            .load(previewUri)
            .placeholder(R.drawable.ic_placeholder)
            .into(binding.coverPlaylist)
        itemView.setOnClickListener {
            clickListener.onClick(model)
        }
    }

    private fun setTracksAmount(context: Context, count: Int): String {
        val countTrack: String = when {
            count % 100 in 11..19 -> context.getString(R.string.track_multiplie)
            count % 10 == 1 -> context.getString(R.string.track_one)
            count % 10 in 2..4 -> context.getString(R.string.track_several)
            else -> context.getString(R.string.track_multiplie)
        }
        return "$count $countTrack"
    }

    fun interface ClickListener {
        fun onClick(playlist: Playlist)
    }

}