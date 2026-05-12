package com.example.playlistmaker

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(
    tracks: List<Track> = emptyList(),
) : RecyclerView.Adapter<TrackViewHolder>() {

    var tracks: List<Track> = tracks
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int = tracks.size
}
