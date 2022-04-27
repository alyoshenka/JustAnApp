package com.alyoshenka.justanapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tl = findViewById<Button>(R.id.button_tl)
        val tr = findViewById<Button>(R.id.button_tr)
        val bl = findViewById<Button>(R.id.button_bl)
        val br = findViewById<Button>(R.id.button_br)

        // tl.setOnClickListener { Toast.makeText(this, resources.getString(R.string.button_tl), Toast.LENGTH_SHORT).show() }
        tr.setOnClickListener { Toast.makeText(this, resources.getString(R.string.button_tr), Toast.LENGTH_SHORT).show() }
        bl.setOnClickListener { Toast.makeText(this, resources.getString(R.string.button_bl), Toast.LENGTH_SHORT).show() }
        br.setOnClickListener { Toast.makeText(this, resources.getString(R.string.button_br), Toast.LENGTH_SHORT).show() }
    }

    fun viewMovies(view: View) {
        intent = Intent(this, ViewMoviesActivity::class.java)
        startActivity(intent)
    }
}