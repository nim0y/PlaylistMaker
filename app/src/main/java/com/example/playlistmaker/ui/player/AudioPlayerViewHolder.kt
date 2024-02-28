package com.example.playlistmaker.ui.player

import android.content.Context
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ViewBottomSheetPlaylistBinding
import com.example.playlistmaker.domain.models.search.Playlist

class AudioPlayerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ViewBottomSheetPlaylistBinding.bind(view)

    fun bind(model: Playlist, clickListener: ClickListener) {
        binding.bottomSheetNamePlaylist.text = model.name
        binding.bottomSheetDecsPlaylist.text = setTracksAmount(itemView.context, model.tracksAmount)
        val previewUri = model.imageUri.let { Uri.parse(it) }

        Glide.with(itemView)
            .load(previewUri)
            .placeholder(R.drawable.ic_placeholder)
            .into(binding.bottomSheetCoverPlaylist)
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