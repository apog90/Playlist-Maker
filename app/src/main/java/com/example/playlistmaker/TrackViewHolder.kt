package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class TrackViewHolder(
    parent: ViewGroup,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false),
) {

    private val artwork: ShapeableImageView = itemView.findViewById(R.id.trackArtwork)
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)

    private val durationFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())
        .apply { timeZone = TimeZone.getTimeZone("UTC") }

    fun bind(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = track.trackTimeMillis?.let { durationFormatter.format(it) }.orEmpty()
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder_track)
            .error(R.drawable.placeholder_track)
            .into(artwork)
    }
}
