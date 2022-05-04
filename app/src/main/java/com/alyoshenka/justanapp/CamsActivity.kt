package com.alyoshenka.justanapp

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CamsActivity : AppCompatActivity() {
    val adapter : CameraAdapter = CameraAdapter()
    var rview : RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cams)

        rview = findViewById(R.id.recyclerView)

        rview?.layoutManager = LinearLayoutManager(this)

        // val adapter = CameraAdapter()
        getAllCams()
    }

    private fun getAllCams() {
        val camsApi = CamsApiClient()
        val camsList = camsApi.getCamsService().getCams()

        camsList.enqueue(object : Callback<CamsResponse> {
            override fun onResponse(
                call: Call<CamsResponse>,
                response: Response<CamsResponse>
            ) {
                if(response.isSuccessful) {
                    val cams = response.body()
                    val allCams = mutableListOf<Cameras>()
                    if (cams != null) {
                        for (feat in cams.Features) {
                            allCams.addAll(feat.Cameras)
                        }

                        adapter.setData(allCams)
                    }
                    rview?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<CamsResponse>, t: Throwable) {
                Log.e("failure", t.localizedMessage)
            }

        })
    }
}