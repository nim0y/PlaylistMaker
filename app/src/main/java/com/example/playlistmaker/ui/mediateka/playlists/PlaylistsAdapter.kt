package com.example.playlistmaker.ui.mediateka.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.search.Playlist

class PlaylistsAdapter(private val clickListener: PlaylistsViewHolder.ClickListener) :
    RecyclerView.Adapter<PlaylistsViewHolder>() {
    var playlists = ArrayList<Playlist>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_playlist, parent, false)
        return PlaylistsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        holder.bind(playlists[position], clickListener)

    }
}