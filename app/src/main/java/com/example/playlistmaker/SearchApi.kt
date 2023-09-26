package com.example.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("/search?entity=song") //&country=ru - что бы поменять страну поиска
    fun trackSearch(@Query("term") text: String): Call<TrackResponse>
}