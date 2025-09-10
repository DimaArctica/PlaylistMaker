package com.practium.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.Runnable
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayer : AppCompatActivity() {

    private lateinit var artworkUrl100: String
    private lateinit var playPauseButton: ImageView
    private lateinit var likeButton: ImageView
    private lateinit var albumCoverPlayer: ImageView
    private lateinit var trackNamePlayer: TextView
    private lateinit var artistNamePlayer: TextView
    private lateinit var trackDurationValue: TextView
    private lateinit var trackAlbumValue: TextView
    private lateinit var trackYearValue: TextView
    private lateinit var trackGenreValue: TextView
    private lateinit var trackCountryValue: TextView
    private lateinit var addToPlaylistButton: ImageView
    private lateinit var trackPlayerTimer: TextView
    private var isTrackLiked = false
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private var mainThreadHandler: Handler? = null
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }


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
        albumCoverPlayer = findViewById<ImageView>(R.id.albumCoverPlayer)
        trackNamePlayer = findViewById<TextView>(R.id.trackNamePlayer)
        artistNamePlayer = findViewById<TextView>(R.id.artistNamePlayer)
        trackDurationValue = findViewById<TextView>(R.id.trackDurationValue)
        trackAlbumValue = findViewById<TextView>(R.id.trackAlbumValue)
        trackYearValue = findViewById<TextView>(R.id.trackYearValue)
        trackGenreValue = findViewById<TextView>(R.id.trackGenreValue)
        trackCountryValue = findViewById<TextView>(R.id.trackCountryValue)
        playPauseButton = findViewById<ImageView>(R.id.playPauseButton)
        likeButton = findViewById<ImageView>(R.id.likeButton)
        addToPlaylistButton = findViewById<ImageView>(R.id.addToPlaylistButton)
        trackPlayerTimer = findViewById<TextView>(R.id.trackPlayerTimer)

        mainThreadHandler = Handler(Looper.getMainLooper())

        val track = intent.getParcelableExtra<Track>(TRACK_KEY)

        trackNamePlayer.setText(track?.trackName)
        artistNamePlayer.setText(track?.artistName)
        trackDurationValue.setText(
            SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(track?.trackTimeMillis)
        )
        trackAlbumValue.setText(track?.collectionName)
        trackYearValue.setText(
            SimpleDateFormat(
                "yyyy",
                Locale.getDefault()
            ).format(track?.releaseDate)
        )
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

        //Подготавливаем и запускаем воспроизведение
        preparePlayer(track?.previewUrl)


        playPauseButton.setOnClickListener {
            playbackControl()
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

    override fun onDestroy() {
        super.onDestroy()
        trackPlayerTimer?.removeCallbacks(updateCurrentPlaybackPosition())
        mediaPlayer.release()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    private fun getCoverArtwork() = artworkUrl100.replaceAfterLast(
        '/', ARTWORK_512_X_512_STRING
    )

    private fun preparePlayer(previewUrl: String?) {

        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
            trackPlayerTimer?.setText(getString(R.string.zero_duration))
        }
        mediaPlayer.setOnCompletionListener {
            playPauseButton.setImageResource(R.drawable.play_button)
            playerState = STATE_PREPARED
            trackPlayerTimer?.removeCallbacks(updateCurrentPlaybackPosition())
            trackPlayerTimer?.setText("00:00")
        }

    }

    private fun startPlayer() {
        mediaPlayer.start()
        playPauseButton.setImageResource(R.drawable.pause_button)
        playerState = STATE_PLAYING
        mainThreadHandler?.post(updateCurrentPlaybackPosition())
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playPauseButton.setImageResource(R.drawable.play_button)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PAUSED -> {
                startPlayer()
            }

            STATE_PREPARED -> {
                startPlayer()
            }
        }
    }

    private fun updateCurrentPlaybackPosition(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    trackPlayerTimer?.setText(
                        dateFormat.format(
                            mediaPlayer.currentPosition
                        )
                    )
                    mainThreadHandler?.postDelayed(this, PLAYBACK_CONTROL_DELAY)
                }
            }
        }
    }


    companion object {
        private const val ARTWORK_512_X_512_STRING = "512x512bb.jpg"
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val PLAYBACK_CONTROL_DELAY = 500L
    }
}