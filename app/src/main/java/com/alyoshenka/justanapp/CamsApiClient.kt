package com.alyoshenka.justanapp

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CamsApiClient {

    private fun getRetrofit(): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://web6.seattle.gov/Travelers/api/Map/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }

    fun getCamsService(): CamsService = getRetrofit().create(CamsService::class.java)
}