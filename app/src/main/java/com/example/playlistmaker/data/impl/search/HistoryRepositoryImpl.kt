package com.example.playlistmaker.data.impl.search

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.domain.api.search.HistoryRepository
import com.example.playlistmaker.domain.models.search.Track
import com.example.playlistmaker.utils.MAX_TEMP_LIST_SIZE
import com.example.playlistmaker.utils.PREFER_SEARCH
import com.example.playlistmaker.utils.PREF_KEY
import com.google.gson.Gson

class HistoryRepositoryImpl(context: Context, private val gson: Gson) : HistoryRepository {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFER_SEARCH, Application.MODE_PRIVATE)

    override fun read(): List<Track> {
        val json = sharedPreferences.getString(PREF_KEY, null) ?: return emptyList()
        return Gson().fromJson(json, Array<Track>::class.java).toList()
    }

    override fun clear() = sharedPreferences.edit().clear().apply()

    override fun add(newTrack: Track) {
        var tempList = read().toMutableList()
        tempList.removeIf { it.trackId == newTrack.trackId }
        tempList.add(0, newTrack)
        if (tempList.size > MAX_TEMP_LIST_SIZE) {
            tempList = tempList.subList(0, MAX_TEMP_LIST_SIZE)
        }
        val json = gson.toJson(tempList)
        sharedPreferences.edit()
            .putString(PREF_KEY, json)
            .apply()
    }
}