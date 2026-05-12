package com.example.playlistmaker

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val sharedPrefs: SharedPreferences) {

    private val gson = Gson()

    fun getHistory(): List<Track> {
        val json = sharedPrefs.getString(HISTORY_KEY, null) ?: return emptyList()
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return gson.fromJson<ArrayList<Track>>(json, type) ?: emptyList()
    }

    fun add(track: Track) {
        val current = ArrayList(getHistory())
        current.removeAll { it.trackId != null && it.trackId == track.trackId }
        current.add(0, track)
        while (current.size > MAX_SIZE) {
            current.removeAt(current.size - 1)
        }
        sharedPrefs.edit { putString(HISTORY_KEY, gson.toJson(current)) }
    }

    fun clear() {
        sharedPrefs.edit { remove(HISTORY_KEY) }
    }

    companion object {
        private const val HISTORY_KEY = "search_history"
        private const val MAX_SIZE = 10
    }
}
