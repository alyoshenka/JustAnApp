package com.alyoshenka.justanapp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class CameraAdapter : RecyclerView.Adapter<CameraAdapter.CameraAdapterViewHolder>() {

    private var URI = "https://www.seattle.gov/trafficcams/images/"

    private var camsList : List<Cameras>? = null
    private var context : Context? = null

    inner class CameraAdapterViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val camName: TextView = view.findViewById(R.id.camera_name)
        val camImg: ImageView = view.findViewById(R.id.camera_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CameraAdapterViewHolder {
        context = parent.context
        return CameraAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.row_cams, parent, false))
    }

    override fun onBindViewHolder(holder: CameraAdapterViewHolder, position: Int) {
        val camsResponse = camsList?.get(position)

        val name = camsResponse?.Description
        val img = URI + camsResponse?.ImageUrl

        holder.camName.text = name
        Picasso.with(holder.camImg.context)
            .load(img)
            .placeholder(R.drawable.car)
            .error(R.drawable.caution)
            .into(holder.camImg, object : Callback {
                override fun onSuccess() { }
                override fun onError() {
                    Log.e("picasso", "Picasso error")

                }
            })
    }

    override fun getItemCount(): Int {
        return camsList?.size ?: 0
    }

    fun setData(camsList : List<Cameras>) {
        this.camsList = camsList
        notifyDataSetChanged()
    }

}