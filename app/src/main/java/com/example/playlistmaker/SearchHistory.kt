package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson


const val PREF_KEY = "pref_key"

class SearchHistory(
    sharedPreferences: SharedPreferences
) {

    private val sharedPref = sharedPreferences
    fun read(): Array<Track> {
        val json = sharedPref.getString(PREF_KEY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    fun clear() = sharedPref.edit().clear().apply()

    fun add(newTrack: Track) {
        var tempList = read().toMutableList()
        tempList.removeIf { it.trackId == newTrack.trackId }
        tempList.add(0, newTrack)
        if (tempList.size > 10) {
            tempList = tempList.subList(0, 10)
        }
        val json = Gson().toJson(tempList)
        sharedPref.edit()
            .putString(PREF_KEY, json)
            .apply()
    }
}