package com.practium.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class TrackListSearchViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val albumCover: ImageView = itemView.findViewById(R.id.albumCover)

    fun bind(track: Track) {
        val radius = itemView.resources.getDimensionPixelSize(R.dimen.corner_radius)
        val trackTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        trackName.text = track.trackName
        artistName.text = itemView.context.getString(R.string.artist_info_template, track.artistName, trackTime)
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .transform(RoundedCorners(radius))
            .placeholder(R.drawable.album_cover_placeholder)
            .into(albumCover)
    }

}
