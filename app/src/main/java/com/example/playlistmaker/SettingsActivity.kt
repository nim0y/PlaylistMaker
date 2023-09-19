package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.app.AppCompatActivity

const val APP_THEME_SHARED_PREFERENCES = "dark_theme_on"
const val IS_DARK_APP_THEME_KEY = "shared_preferences_status"

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val switchcompat = findViewById<SwitchCompat>(R.id.switchcompat)
        val backButton = findViewById<Button>(R.id.buttonBack)
        val sharingButton = findViewById<Button>(R.id.shareButton)
        val supportButton = findViewById<Button>(R.id.supportButton)

        switchcompat.isChecked = (applicationContext as App).switchOn

        switchcompat.setOnCheckedChangeListener { _, isChecked ->
            (applicationContext as App).themeToggle(isChecked)

            val sPref = getSharedPreferences(APP_THEME_SHARED_PREFERENCES, MODE_PRIVATE)
            sPref.edit()
                .putBoolean(IS_DARK_APP_THEME_KEY, isChecked)
                .apply()
        }

        backButton.setOnClickListener {
            finish()
        }

        sharingButton.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_button_url))
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        supportButton.setOnClickListener {
            val subject = getString(R.string.support_mail_subject)
            val text = getString(R.string.support_mail_text)
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.dev_email)))
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.putExtra(Intent.EXTRA_TEXT, text)
            startActivity(intent)
        }

        val termsOfUseButton = findViewById<Button>(R.id.termsOfUseButton)
        termsOfUseButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.offer_yandex)))
            startActivity(intent)
        }
    }
}

