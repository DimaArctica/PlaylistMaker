package com.practium.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

const val SEARCH_HISTORY_LIST = "search_history_list"

class SearchHistory(private val sharedPrefs: SharedPreferences) {

    private var searchHistory: ArrayList<Track> = ArrayList()

    fun addTrackToSearchHistory(track: Track){
        searchHistory = getSearchHistory()

    }

    fun clearSearchHistory(){
        searchHistory.clear()
    }

    fun getSearchHistory(): ArrayList<Track>{
        val json = sharedPrefs.getString(SEARCH_HISTORY_LIST, null)
        return Gson().fromJson(json, ArrayList<Track>()::class.java)
    }

}