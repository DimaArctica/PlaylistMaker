package com.practium.playlistmaker

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val trackId: Int,
    val country: String,
    val primaryGenreName: String,
    val collectionName: String,
    val releaseDate: Date
) : Parcelable
