package com.practicum.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class ActivityPlayer : AppCompatActivity() {

    private lateinit var track: Track
    private lateinit var ibPlayStop: ImageButton

    private lateinit var tvCurrentPosition:TextView
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT

    private var currentPosition = 0
    private val handler = Handler(Looper.getMainLooper())
    private val dateFormat: SimpleDateFormat by lazy {
        SimpleDateFormat("mm:ss", Locale.getDefault())
    }
    private val positionRunnable = object : Runnable {
        override fun run() {
            if (playerState == STATE_PLAYING) {
                currentPosition = mediaPlayer.currentPosition
                tvCurrentPosition.text = dateFormat.format(currentPosition)
                handler.postDelayed(this, POSITION_UPDATE_INTERVAL)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbarPlayer = findViewById<Toolbar>(R.id.toolbarPlayer)
        val ivCover = findViewById<ImageView>(R.id.ivCover)
        val tvTrackName = findViewById<TextView>(R.id.tvTrackName)
        val tvAlbum = findViewById<TextView>(R.id.tvAlbum)
        val timeTrack = findViewById<TextView>(R.id.timeTrack)
        val album = findViewById<TextView>(R.id.album)
        val year = findViewById<TextView>(R.id.year)
        val genre = findViewById<TextView>(R.id.genre)
        val country = findViewById<TextView>(R.id.country)
        val buttonGroupAlbom = findViewById<Group>(R.id.buttonGroupAlbom)
        val buttonGroupYear = findViewById<Group>(R.id.buttonGroupYear)
        tvCurrentPosition = findViewById(R.id.tvCurrentPosition)
        ibPlayStop = findViewById(R.id.ibPlayStop)

        //можно заменить на intent.getParcelableExtra<Track>("TRACK", Track::class.java), но тогда нужно поднимать API до API 33+
        track = intent.getParcelableExtra("TRACK")!!

        Glide.with(this)
            .load(track.getCoverArtwork())
            .centerCrop()
            .transform(RoundedCorners(dpToPx(8f)))
            .placeholder(R.drawable.ic_placeholder_312)
            .error(R.drawable.ic_placeholder_312)
            .into(ivCover)

        tvTrackName.text = track.trackName
        tvAlbum.text = track.collectionName
        timeTrack.text = dateFormat.format(track.trackTime)
        album.text = track.collectionName
        year.text = track.releaseDate.substring(0, 4)
        genre.text = track.primaryGenreName
        country.text = track.country

        toolbarPlayer.setOnClickListener {
            finish()
        }

        if (track.collectionName.isEmpty()) buttonGroupAlbom.visibility = View.GONE
        if (track.releaseDate.isEmpty()) buttonGroupYear.visibility = View.GONE

        preparePlayer()

        ibPlayStop.setOnClickListener {
            playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(positionRunnable)
        mediaPlayer.release()

    }

    private fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            resources.displayMetrics
        ).toInt()
    }

    private fun preparePlayer() {

        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            ibPlayStop.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            ibPlayStop.setImageResource(R.drawable.ic_play_83)
            playerState = STATE_PREPARED
            tvCurrentPosition.text = dateFormat.format(0)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        ibPlayStop.setImageResource(R.drawable.ic_pause_84)
        playerState = STATE_PLAYING
        handler.post(positionRunnable)

    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        ibPlayStop.setImageResource(R.drawable.ic_play_83)
        playerState = STATE_PAUSED
        handler.removeCallbacks(positionRunnable)
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val POSITION_UPDATE_INTERVAL  = 350L
    }
}