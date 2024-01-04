package com.example.browserapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.browserapp.R
import com.example.browserapp.dataClasses.bingSearch.BingSearch
import com.example.browserapp.subscriptionKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchAdapter(private val searchItems: List<BingSearch.WebPages.Value?>?) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the item layout dynamically
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem:BingSearch.WebPages.Value? = searchItems?.get(position)
        // Bind data to the views in the item layout
        if (currentItem?.thumbnailUrl != null) {
            holder.bindImage(currentItem.thumbnailUrl)
            holder.thumbnailImage.visibility = View.VISIBLE // Show image view
        } else {
            holder.thumbnailImage.visibility = View.GONE // Hide image view
        }
        holder.name.text = currentItem?.name
        holder.displayUrl.text = currentItem?.displayUrl
        holder.datePublished.text = currentItem?.datePublished
        holder.snippet.text = currentItem?.snippet
        // ... (bind other views as needed)
    }

    override fun getItemCount(): Int = searchItems?.size ?: 0
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //        name, displayUrl,datePublished,snippet
        // ... (reference other views in the item layout)
        val thumbnailImage: ImageView = view.findViewById(R.id.thumbnailImage)
        val name: TextView = view.findViewById(R.id.name)
        val displayUrl: TextView = view.findViewById(R.id.displayUrl)
        val datePublished:TextView= view.findViewById(R.id.datePublished)
        val snippet: TextView = view.findViewById(R.id.snippet)

        fun bindImage(imageUrl: String?) {
            Glide.with(itemView.context)
                .load(imageUrl)
                .into(thumbnailImage)
        }

    }
}
