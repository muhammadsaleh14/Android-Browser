package com.example.browserapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.browserapp.R
import com.example.browserapp.dataClasses.bingSearch.BingSearch

class SearchAdapter(private val searchItems: List<BingSearch.WebPages.Value?>?) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the item layout dynamically
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = searchItems?.get(position)
        // Bind data to the views in the item layout

        holder.bindImage(currentItem?.thumbnailUrl)

        // ... (bind other views as needed)
    }

    override fun getItemCount(): Int = searchItems?.size ?: 0

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        // ... (reference other views in the item layout)
        val thumbnailImage: ImageView = view.findViewById(R.id.thumbnailImage)
        val snippet: TextView = view.findViewById(R.id.snippet)
        val displayUrl: TextView = view.findViewById(R.id.displayUrl)

        fun bindImage(imageUrl: String?) {
            Glide.with(itemView.context)
                .load(imageUrl)
                .into(thumbnailImage)
        }

    }
}
