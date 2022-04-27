package com.alyoshenka.justanapp

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MovieViewAdapter(private val data: ArrayList<Movie>, val click: (movie: Movie) -> Unit) : RecyclerView.Adapter<MovieViewAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.list_movie_title)
        val year: TextView = view.findViewById(R.id.list_movie_year)

        init {
            view.setOnClickListener {
                Log.d("testing", "click")
                click(data[position])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_movie_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = data[position].title
        holder.year.text = data[position].year
    }

    override fun getItemCount(): Int {
        return data.size
    }
}