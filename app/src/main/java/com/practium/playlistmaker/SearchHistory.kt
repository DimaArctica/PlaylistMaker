package com.practium.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val SEARCH_HISTORY_LIST = "search_history_list"
const val SEARCH_HISTORY_COUNT = 10

class SearchHistory {

    private var searchHistory: ArrayList<Track> = ArrayList()
    private val type = object : TypeToken<ArrayList<Track>>() {}.type

    fun addTrackToSearchHistory(sharedPrefs: SharedPreferences, track: Track): ArrayList<Track>{
        for (trackTemp in searchHistory) {
            if (trackTemp.trackId == track.trackId) {
                searchHistory.remove(trackTemp)
                searchHistory.add(0, track)
                savePrefs(sharedPrefs)
                return searchHistory
            }
        }
        if (searchHistory.count() >= SEARCH_HISTORY_COUNT) {
            searchHistory.removeAt(SEARCH_HISTORY_COUNT - 1)
        }
        searchHistory.add(0, track)
        savePrefs(sharedPrefs)

        return searchHistory
    }

    fun clearSearchHistory(sharedPrefs: SharedPreferences){
        searchHistory.clear()
        sharedPrefs.edit().
            clear().
            apply()
    }

    fun getSearchHistoryFromPrefs(sharedPrefs: SharedPreferences): ArrayList<Track>{
        val json = sharedPrefs.getString(SEARCH_HISTORY_LIST, null)
        val emptySearchHistoryArray: ArrayList<Track> = ArrayList<Track>()
        if (json != null) {
            searchHistory.addAll(Gson().fromJson(json, type))
        } else {
            return emptySearchHistoryArray
        }
        return searchHistory
    }

    fun getSearchHistory(): ArrayList<Track>{
        return searchHistory
    }

    private fun savePrefs(sharedPrefs: SharedPreferences) {
        val json = Gson().toJson(searchHistory)
        sharedPrefs.edit().
            clear().
            apply()
        sharedPrefs.edit()
            .putString(SEARCH_HISTORY_LIST,json)
            .apply()
    }

}