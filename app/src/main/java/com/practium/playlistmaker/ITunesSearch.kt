package com.practium.playlistmaker

data class ITunesSearch(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String)
