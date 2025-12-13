package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import android.content.res.Configuration

const val DARK_THEME = "darkTheme"
const val DARK_THEME_KEY = "key_for_dark_theme"

class App : Application() {

    var isDarkTheme: Boolean = false
        private set

    lateinit var sharedPrefs: SharedPreferences
        private set

    override fun onCreate() {
        super.onCreate()

        sharedPrefs = getSharedPreferences(DARK_THEME, MODE_PRIVATE)
        isDarkTheme = sharedPrefs.getBoolean(DARK_THEME_KEY, isSystemInDarkTheme())

        switchTheme(isDarkTheme)
    }

    //реализованно через побитовую операцию, для того что бы до использования switch применялась тема из системы
    fun isSystemInDarkTheme(): Boolean {
        return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }

    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        isDarkTheme = darkThemeEnabled
        sharedPrefs.edit().putBoolean(DARK_THEME_KEY, isDarkTheme).apply()

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

    }
}