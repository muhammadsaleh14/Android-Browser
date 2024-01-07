package com.example.browserapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.browserapp.R
import com.example.browserapp.dataClasses.bingSearch.WebpagesSearch
import com.example.browserapp.listeners.webpagesListener

class WebpagesSearchAdapter(private val searchItems: List<WebpagesSearch.WebPages.Value?>?) :
    RecyclerView.Adapter<WebpagesSearchAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the item layout dynamically
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_websearch, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem:WebpagesSearch.WebPages.Value? = searchItems?.get(position)
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
        webpagesListener(holder.webpageLinearLayout,holder.itemView.context,currentItem?.url)
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
        val webpageLinearLayout: LinearLayout = view.findViewById(R.id.webpageLinearLayout)

        fun bindImage(imageUrl: String?) {
            Glide.with(itemView.context)
                .load(imageUrl)
                .into(thumbnailImage)
        }

    }
}
