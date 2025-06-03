package com.practium.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackListSearchAdapter(
    private val trackList: ArrayList<ITunesSearch>
) : RecyclerView.Adapter<TrackListSearchViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListSearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_track_list_view, parent, false)
        return TrackListSearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackListSearchViewHolder, position: Int) {
        holder.bind(trackList[position])
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}
