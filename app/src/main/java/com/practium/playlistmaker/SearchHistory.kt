package com.practium.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

const val SEARCH_HISTORY_LIST = "search_history_list"

class SearchHistory(private val sharedPrefs: SharedPreferences) {

    private var searchHistory: ArrayList<Track> = ArrayList()

    fun addTrackToSearchHistory(track: Track){
        searchHistory = getSearchHistory()
        for (trackTemp in searchHistory) {
            if (trackTemp.trackId == track.trackId) {
                searchHistory.remove(trackTemp)
                searchHistory.add(track)
                return
            }
        }
        if (searchHistory.count() == 10) {
            searchHistory.removeAt(10)
        }
        searchHistory.add(track)
    }

    fun clearSearchHistory(){
        searchHistory.clear()
        val json = Gson().toJson(searchHistory)
        sharedPrefs.edit()
            .putString(SEARCH_HISTORY_LIST,json)
    }

    fun getSearchHistory(): ArrayList<Track>{
        val json = sharedPrefs.getString(SEARCH_HISTORY_LIST, null)
        return Gson().fromJson(json, ArrayList<Track>()::class.java)
    }

}