package com.practium.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val shareButton = findViewById<FrameLayout>(R.id.shareButton)
        val supportButton = findViewById<FrameLayout>(R.id.supportButton)
        val userAgreementButton = findViewById<FrameLayout>(R.id.userAgreementButton)

        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message))
            shareIntent.type = "text/plain"
            startActivity(shareIntent)
        }

        supportButton.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email_address)))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_email_subject))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_email_text))
            startActivity(supportIntent)
        }

        userAgreementButton.setOnClickListener {
            val userAgreementIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.user_agreement_url)))
            startActivity(userAgreementIntent)
        }

    }
}