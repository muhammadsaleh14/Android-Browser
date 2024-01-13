package com.example.browserapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.browserapp.R
import com.example.browserapp.dataClasses.bingSearch.WebpagesSearch
import com.example.browserapp.listeners.webpagesListener

class WebpagesSearchAdapter(diffCallback: DiffUtil.ItemCallback<WebpagesSearch.WebPages.Value>) :
    PagingDataAdapter<WebpagesSearch.WebPages.Value, WebpagesSearchAdapter.ViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the item layout dynamically
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_websearch, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val currentItem: WebpagesSearch.WebPages.Value? = getItem(position)
            // Bind data to the views in the item layout
            if (currentItem?.thumbnailUrl != null) {
                holder.bindImage(currentItem.thumbnailUrl)
                holder.thumbnailImage.visibility = View.VISIBLE // Show image view
            } else {
                holder.thumbnailImage.visibility = View.GONE // Hide image view
            }
            holder.name.text = currentItem?.name
            holder.displayUrl.text = currentItem?.displayUrl
            if (currentItem?.datePublished != null) {
                holder.datePublished.text = currentItem?.datePublished
                holder.datePublished.visibility = View.VISIBLE // Show image view
            } else {
                holder.datePublished.visibility = View.GONE // Hide image view
            }
            holder.snippet.text = currentItem?.snippet
            webpagesListener(holder.webpageLinearLayout, holder.itemView.context, currentItem?.url,currentItem?.name)
            // ... (bind other views as needed)
        } catch (e: Exception) {
            Log.e("TAGINN2", e.stackTraceToString())
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //        name, displayUrl,datePublished,snippet
        // ... (reference other views in the item layout)
        val thumbnailImage: ImageView = view.findViewById(R.id.thumbnailImage)
        val name: TextView = view.findViewById(R.id.name)
        val displayUrl: TextView = view.findViewById(R.id.displayUrl)
        val datePublished: TextView = view.findViewById(R.id.datePublished)
        val snippet: TextView = view.findViewById(R.id.snippet)
        val webpageLinearLayout: LinearLayout = view.findViewById(R.id.webpageLinearLayout)
        fun bindImage(imageUrl: String?) {
            Glide.with(itemView.context)
                .load(imageUrl)
                .into(thumbnailImage)
        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WebpagesSearch.WebPages.Value>() {
            override fun areItemsTheSame(
                oldItem: WebpagesSearch.WebPages.Value,
                newItem: WebpagesSearch.WebPages.Value
            ): Boolean {
                // Return true if items represent the same web page
                val bool = oldItem.url == newItem.url
                Log.d("TAGINN2", "are items the same:$bool")
                return bool
            }

            override fun areContentsTheSame(
                oldItem: WebpagesSearch.WebPages.Value,
                newItem: WebpagesSearch.WebPages.Value
            ): Boolean {
                // Return true if items have the same content (name, url, etc.)
                val bool = oldItem == newItem
                Log.d("TAGINN2", "are contents the same:$bool")
                return bool
            }
        }
    }
}
