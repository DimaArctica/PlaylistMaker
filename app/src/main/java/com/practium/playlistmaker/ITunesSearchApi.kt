package com.practium.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesSearchApi {

    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<ITunesSearchResponse>
}