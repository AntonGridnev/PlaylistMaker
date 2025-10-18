package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActivitySettings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbarSetting = findViewById<Toolbar>(R.id.toolbar_setting)
        val darkThemeSwitch = findViewById<SwitchCompat>(R.id.dark_theme_switch)
        val share = findViewById<View>(R.id.share)
        val writeToSupport = findViewById<View>(R.id.write_to_support)
        val userAgreement = findViewById<View>(R.id.user_agreement)

        toolbarSetting.setNavigationOnClickListener  {
            finish()
        }

        darkThemeSwitch.setOnClickListener {

        }

        share.setOnClickListener{
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message))
            startActivity(shareIntent)
        }

        writeToSupport.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.write_to_support_email)))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.write_to_support_subject))
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.write_to_support_text))
            startActivity(shareIntent)
        }

        userAgreement.setOnClickListener {
            val userAgreementIntent = Intent(Intent.ACTION_VIEW)
            userAgreementIntent.data = Uri.parse(getString(R.string.user_agreement_link))
            startActivity(userAgreementIntent)
        }
    }
}