package com.practicum.playlistmaker

import android.provider.ContactsContract.Contacts.Data
import com.google.gson.annotations.SerializedName

data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: Long,
    val artworkUrl100: String
)
