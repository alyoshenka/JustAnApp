package com.alyoshenka.justanapp

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bl = findViewById<Button>(R.id.button_bl)
        val br = findViewById<Button>(R.id.button_br)

        bl.setOnClickListener { startActivity(Intent(this, TrafficCameraMapActivity::class.java)) }
        br.setOnClickListener { Toast.makeText(this, resources.getString(R.string.button_br), Toast.LENGTH_SHORT).show() }
    }

    fun viewMovies(view : View) {
        intent = Intent(this, ViewMoviesActivity::class.java)
        startActivity(intent)
    }

    fun viewCams(view : View) {
        val connection : ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activityInfo = connection.activeNetworkInfo
        if(activityInfo != null && activityInfo.isConnected) {
            intent = Intent(this, CamsActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show()
        }
    }
}