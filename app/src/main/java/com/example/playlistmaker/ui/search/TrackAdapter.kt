package com.example.playlistmaker.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.search.Track

class TrackAdapter() : RecyclerView.Adapter<TrackViewHolder>() {

    var tracksList: ArrayList<Track> = ArrayList()
    lateinit var itemClickListener: ((Track) -> Unit)

    fun interface onItemClickListener {
        fun onItemClick(track: Track)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tracksList.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracksList[position])
        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(tracksList[position])
            this.notifyItemRangeChanged(0, tracksList.size)
        }
    }
}


//private var isClickAllowed = true
//private val handler = Handler(Looper.getMainLooper())
//private fun clickDebounce(): Boolean {
//    val current = isClickAllowed
//    if (isClickAllowed) {
//        isClickAllowed = false
//        handler.postDelayed({ isClickAllowed = true }, DEBOUNCE_DELAY)
//    }
//    return current
//}
//
//private fun openAudioPlayer(holder: TrackViewHolder, track: Track) {
//    if (clickDebounce()) {
//        val intent = Intent(holder.itemView.context, AudioPlayerActivity::class.java)
//        intent.putExtra(CURRENT_TRACK, Gson().toJson(track))
//        holder.itemView.context.startActivity(intent)
//    }
