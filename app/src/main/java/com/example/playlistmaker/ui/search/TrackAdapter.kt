package com.example.playlistmaker.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.search.Track

class TrackAdapter : RecyclerView.Adapter<TrackViewHolder>() {

    var tracksList: ArrayList<Track> = ArrayList()
    lateinit var itemClickListener: ((Track) -> Unit)
    lateinit var itemLongClickListener: ((Track) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tracksList.size
    }

    fun setTracks(tracks: List<Track>) {
        tracksList.clear()
        tracksList.addAll(tracks)
        notifyItemRangeChanged(0, tracksList.size)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracksList[position])
        holder.itemView.setOnClickListener {
            itemClickListener.invoke(tracksList[position])
            this.notifyItemRangeChanged(0, tracksList.size)
        }
        holder.itemView.setOnLongClickListener {
            itemLongClickListener.invoke(tracksList[position])
            true
        }
    }
}