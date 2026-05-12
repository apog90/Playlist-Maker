package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

class App : Application() {

    lateinit var sharedPrefs: SharedPreferences
        private set

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        sharedPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        darkTheme = if (sharedPrefs.contains(DARK_THEME_KEY)) {
            sharedPrefs.getBoolean(DARK_THEME_KEY, false)
        } else {
            val currentNightMode =
                resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            currentNightMode == Configuration.UI_MODE_NIGHT_YES
        }
        applyTheme()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        sharedPrefs.edit { putBoolean(DARK_THEME_KEY, darkThemeEnabled) }
        applyTheme()
    }

    private fun applyTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        private const val PREFS_NAME = "playlist_maker_prefs"
        private const val DARK_THEME_KEY = "dark_theme"
    }
}
