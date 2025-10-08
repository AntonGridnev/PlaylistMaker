package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val search = findViewById<Button>(R.id.search_main)
        search.setOnClickListener {
            val displaySearch = Intent(this, ActivitySearch::class.java)
            startActivity(displaySearch)
        }

        val mediaLibrary = findViewById<Button>(R.id.media_library_main)
        mediaLibrary.setOnClickListener {
            val displayMediaLibrary = Intent(this, ActivityMediaLibrary::class.java)
            startActivity(displayMediaLibrary)
        }

        val settings = findViewById<Button>(R.id.settings_main)
        settings.setOnClickListener {
            val displaySettings = Intent(this, ActivitySettings::class.java)
            startActivity(displaySettings)
        }
    }
}
