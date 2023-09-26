package com.example.playlistmaker

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

const val CURRENT_TRACK = "current_track"

class TrackAdapter(
    private val tracksList: List<Track>,
    private val itemClickListener: (Track) -> Unit
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_track, parent, false)
        return TrackViewHolder(view, itemClickListener)
    }

    override fun getItemCount(): Int {
        return tracksList.size

    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracksList[position])
        holder.setIsRecyclable(true)
        holder.itemView.setOnClickListener {
            openAudioPlayer(holder, tracksList[position])
            itemClickListener.invoke(tracksList[position])
            Log.e("myLog", "Track push $position")
        }
    }
}

private fun openAudioPlayer(holder: TrackViewHolder, track: Track) {
    val intent = Intent(holder.itemView.context, AudioPlayerActivity::class.java)
    intent.putExtra(CURRENT_TRACK, Gson().toJson(track))
    holder.itemView.context.startActivity(intent)
}