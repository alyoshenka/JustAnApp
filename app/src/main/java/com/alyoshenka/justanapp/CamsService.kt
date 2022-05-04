package com.alyoshenka.justanapp

import retrofit2.Call
import retrofit2.http.GET

interface CamsService {
    @GET("Data?zoomId=13&type=2")
    fun getCams() : Call<CamsResponse>
}