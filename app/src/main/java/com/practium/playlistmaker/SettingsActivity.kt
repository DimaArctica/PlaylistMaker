package com.practium.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        val shareButton = findViewById<FrameLayout>(R.id.shareButton)

        shareButton.setOnClickListener {
//            val message = "https://practicum.yandex.ru/profile/android-developer-plus/"
//            val shareIntent = Intent(Intent.ACTION_CHOOSER)
//            //shareIntent.action = Intent.ACTION_SEND
//            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
//            startActivity(intent)
            val message = "Привет, Android-разработка — это круто!"
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("")
            //shareIntent.putExtra(Intent.EXTRA_, arrayOf("yourEmail@ya.ru"))
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(shareIntent)
        }

    }
}