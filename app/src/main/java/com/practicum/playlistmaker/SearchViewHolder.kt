package com.practicum.playlistmaker

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackName: TextView = itemView.findViewById(R.id.TrackName)
    private val artistName: TextView = itemView.findViewById(R.id.ArtistName)
    private val trackTime: TextView = itemView.findViewById(R.id.TrackTime)
    private val artworkUrl100: ImageView = itemView.findViewById(R.id.ArtworkUrl100)

    constructor(parent: ViewGroup) : this(
        LayoutInflater.from(parent.context).inflate(R.layout.search_view, parent, false)
    )

    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = formatTrackTime(model.trackTime)
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(itemView.dpToPx(2f)))
            .placeholder(R.drawable.ic_placeholder_45)
            .error(R.drawable.ic_placeholder_45)
            .into(artworkUrl100)
    }

    private fun View.dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            this.context.resources.displayMetrics
        ).toInt()
    }

    private fun formatTrackTime(time: Any?): String {
        return try {
            when (time) {
                is Long -> {
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
                }
                is Int -> {
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(time.toLong())
                }
                is Double -> {
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(time.toLong())
                }
                is String -> {
                    val longTime = time.toLongOrNull()
                    if (longTime != null) {
                        SimpleDateFormat("mm:ss", Locale.getDefault()).format(longTime)
                    } else {
                        time
                    }
                }
                else -> {
                    ""
                }
            }
        } catch (e: Exception) {
            ""
        }
    }
}
