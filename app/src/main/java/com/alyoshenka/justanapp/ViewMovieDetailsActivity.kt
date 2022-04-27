package com.alyoshenka.justanapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class ViewMovieDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_movie_details)

        title = "Back to Movie List"

        findViewById<TextView>(R.id.movie_detail_title).text = intent.getStringExtra("title")
        findViewById<TextView>(R.id.movie_detail_year).text = intent.getStringExtra("year")
        findViewById<TextView>(R.id.movie_detail_director).text = intent.getStringExtra("director")
        findViewById<TextView>(R.id.movie_detail_description).text = intent.getStringExtra("description")
    }
}