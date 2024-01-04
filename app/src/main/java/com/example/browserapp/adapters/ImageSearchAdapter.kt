package com.example.browserapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.executor.GlideExecutor.UncaughtThrowableStrategy.LOG
import com.example.browserapp.R
import com.example.browserapp.dataClasses.bingSearch.ImagesSearch

class ImageSearchAdapter(private val searchItems: List<ImagesSearch.Value?>?) :
    RecyclerView.Adapter<ImageSearchAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the item layout dynamically
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_imagesearch, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem:ImagesSearch.Value? = searchItems?.get(position)
        // Bind data to the views in the item layout
        if (currentItem?.thumbnailUrl != null) {
            holder.bindImage(currentItem.thumbnailUrl)
            holder.thumbnailImage.visibility = View.VISIBLE // Show image view
        } else {
            holder.thumbnailImage.visibility = View.GONE // Hide image view
        }

    //        holder.name.text = currentItem?.name
    }

    override fun getItemCount(): Int = searchItems?.size ?: 0
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //        name, displayUrl,datePublished,snippet
        // ... (reference other views in the item layout)
        val thumbnailImage: ImageView = view.findViewById(R.id.searchedImage)



        fun bindImage(imageUrl: String?) {
            Log.d("TAGINN",imageUrl?:"")
            Glide.with(itemView.context)
                .load(imageUrl)
                .into(thumbnailImage)
        }

    }
}
