package com.practicum.playlistmaker

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val SEARCH_HISTORY_KEY = "key_for_search_history"
private const val MAX_HISTORY_SIZE = 10

class SearchHistory(private val sharedPrefs: SharedPreferences) {

    private val listeners = mutableSetOf<() -> Unit>()
    private var isListenerRegistered = false

    init {
        sharedPrefs.registerOnSharedPreferenceChangeListener { _, key ->
            if (key == SEARCH_HISTORY_KEY) {
                notifyAllListeners()
            }
        }
        isListenerRegistered = true
    }

    fun addChangeListener(listener: () -> Unit) {
        listeners.add(listener)
    }

    fun removeChangeListener(listener: () -> Unit) {
        listeners.remove(listener)
    }

    private fun notifyAllListeners() {
        listeners.forEach { it() }
    }

    fun addTrack(track: Track) {
        val history = getHistory().toMutableList()

        history.removeAll { it.trackId == track.trackId }

        history.add(0, track)

        if (history.size > MAX_HISTORY_SIZE) {
            history.removeAt(history.lastIndex)
        }

        val json = Gson().toJson(history)
        sharedPrefs.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()

        notifyAllListeners()
    }

    fun getHistory(): MutableList<Track> {
        val json = sharedPrefs.getString(SEARCH_HISTORY_KEY, null) ?: return mutableListOf()
        val array = Gson().fromJson(json, Array<Track>::class.java) ?: return mutableListOf()
        return array.toMutableList()
    }

    fun clearHistory() {
        sharedPrefs.edit().remove(SEARCH_HISTORY_KEY).apply()
    }
}