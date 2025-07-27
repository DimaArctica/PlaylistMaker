package com.practium.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.appbar.MaterialToolbar
import java.text.SimpleDateFormat
import java.util.Locale

private lateinit var artworkUrl100: String
private var isTrackPlaying = false
private var isTrackLiked = false

class AudioPlayer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_audio_player)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activityAudioPlayer)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val arrowBackButtonPlayer = findViewById<MaterialToolbar>(R.id.arrowBackButtonPlayer)
        val albumCoverPlayer = findViewById<ImageView>(R.id.albumCoverPlayer)
        val trackNamePlayer = findViewById<TextView>(R.id.trackNamePlayer)
        val artistNamePlayer = findViewById<TextView>(R.id.artistNamePlayer)
        val trackDurationValue = findViewById<TextView>(R.id.trackDurationValue)
        val trackAlbumValue = findViewById<TextView>(R.id.trackAlbumValue)
        val trackYearValue = findViewById<TextView>(R.id.trackYearValue)
        val trackGenreValue = findViewById<TextView>(R.id.trackGenreValue)
        val trackCountryValue = findViewById<TextView>(R.id.trackCountryValue)
        val playPauseButton = findViewById<ImageView>(R.id.playPauseButton)
        val likeButton = findViewById<ImageView>(R.id.likeButton)
        val addToPlaylistButton = findViewById<ImageView>(R.id.addToPlaylistButton)

        val track = intent.getParcelableExtra<Track>(TRACK_KEY)

        trackNamePlayer.setText(track?.trackName)
        artistNamePlayer.setText(track?.artistName)
        trackDurationValue.setText(SimpleDateFormat("mm:ss", Locale.getDefault()).format(track?.trackTimeMillis))
        trackAlbumValue.setText(track?.collectionName)
        trackYearValue.setText(SimpleDateFormat("yyyy", Locale.getDefault()).format(track?.releaseDate))
        trackGenreValue.setText(track?.primaryGenreName)
        trackCountryValue.setText(track?.country)
        artworkUrl100 = track?.artworkUrl100.toString()

        Glide.with(applicationContext)
            .load(getCoverArtwork())
            .placeholder(R.drawable.album_cover_placeholder)
            .into(albumCoverPlayer)

        arrowBackButtonPlayer.setNavigationOnClickListener {
            finish()
        }

        playPauseButton.setOnClickListener {
            if (!isTrackPlaying) {
                playPauseButton.setImageResource(R.drawable.pause_button)
            } else {
                playPauseButton.setImageResource(R.drawable.play_button)
            }
            isTrackPlaying = !isTrackPlaying
        }

        likeButton.setOnClickListener {
            if (isTrackLiked) {
                likeButton.setImageResource(R.drawable.unliked_button)
            } else {
                likeButton.setImageResource(R.drawable.liked_button)
            }
            isTrackLiked = !isTrackLiked
        }

        addToPlaylistButton.setOnClickListener {
            // TODO:  
        }

    }

    private fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/', ARTWORK_512_X_512_STRING)

    companion object {
        const val ARTWORK_512_X_512_STRING = "512x512bb.jpg"
    }
}