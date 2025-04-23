package com.practium.playlistmaker

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackListSearchViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val albumCover: ImageView = itemView.findViewById(R.id.albumCover)

    @SuppressLint("SetTextI18n", "CheckResult")
    fun bind(track: Track) {
        trackName.text = track.trackName
        artistName.text = "${track.artistName}  •  ${track.trackTime}"
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.album_cover_placeholder)
            .into(albumCover)
    }

}
