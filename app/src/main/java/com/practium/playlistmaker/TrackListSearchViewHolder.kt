package com.practium.playlistmaker

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TrackListSearchViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val artwork: ImageView = itemView.findViewById<ImageView>(R.id.albumCover)

    @SuppressLint("SetTextI18n")
    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = "${model.artistName} ● ${model.trackTime}"
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.album_cover_placeholder)
            .into(artwork)
    }

}
