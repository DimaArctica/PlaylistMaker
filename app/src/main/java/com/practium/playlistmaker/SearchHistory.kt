package com.practium.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

const val SEARCH_HISTORY_LIST = "search_history_list"
const val SEARCH_HISTORY_COUNT = 10

class SearchHistory {

    private var searchHistory: ArrayList<Track> = ArrayList()

    fun addTrackToSearchHistory(sharedPrefs: SharedPreferences, track: Track){
        //searchHistory = getSearchHistoryFromPrefs(sharedPrefs)
        for (trackTemp in searchHistory) {
            if (trackTemp.trackId == track.trackId) {
                searchHistory.remove(trackTemp)
                searchHistory.add(track)
                return
            }
        }
        if (searchHistory.count() >= SEARCH_HISTORY_COUNT) {
            searchHistory.removeAt(SEARCH_HISTORY_COUNT - 1)
        }
        searchHistory.add(0, track)
        //savePrefs(sharedPrefs)
        return
    }

    fun clearSearchHistory(sharedPrefs: SharedPreferences): ArrayList<Track>{
        searchHistory.clear()
//        sharedPrefs.edit().
//            clear().
//            apply()
        return searchHistory
    }

    fun getSearchHistoryFromPrefs(sharedPrefs: SharedPreferences): ArrayList<Track>{
        val json = sharedPrefs.getString(SEARCH_HISTORY_LIST, null)
        if (json != null) {
            return Gson().fromJson(json, ArrayList<Track>()::class.java)
        } else {
            return ArrayList<Track>()
        }
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