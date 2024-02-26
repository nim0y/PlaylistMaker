package com.example.playlistmaker.ui.mediateka.playlists

import android.content.Context
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
        val previewUri = model.imageUri

        Glide.with(itemView)
            .load(previewUri)
            .placeholder(R.drawable.ic_placeholder)
            .into(binding.coverPlaylist)
        itemView.setOnClickListener {
            clickListener.onClick(model)
        }
    }

    private fun setTracksAmount(context: Context, count: Int): String {
        val countTrack = context.resources.getQuantityString(R.plurals.track_count, count)
        return "$count $countTrack"
    }

    fun interface ClickListener {
        fun onClick(playlist: Playlist)
    }

}