package com.example.browserapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.browserapp.R
import com.example.browserapp.dataClasses.bingSearch.ImagesSearch
import com.example.browserapp.fragments.ImagesFragment
import com.example.browserapp.viewmodels.ImageDetails

class ImageSearchAdapter(
    private val imagesFragment: ImagesFragment,
    private val searchItems: List<ImagesSearch.Value?>?
) :
    RecyclerView.Adapter<ImageSearchAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the item layout dynamically
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_image_search, parent, false)
//        Log.d("TAGINN1",view.toString())
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("TAGINN1", holder.toString())
        val currentItem: ImagesSearch.Value? = searchItems?.get(position)
        // Bind data to the views in the item layout
        if (currentItem?.hostPageDisplayUrl != null) {
            holder.bindImage(currentItem.thumbnailUrl)
            holder.thumbnailImage.visibility = View.VISIBLE // Show image view

            holder.thumbnailImage.setOnClickListener {
                try {
                    if (!ImageDetails.isImageDetailFragmentOpen) {
//                        holder.itemView.setOnClickListener{
//                                ImageDetails.signalClose()
//                        }
                        ImageDetails.name = currentItem?.name
                        ImageDetails.hostPageDisplayUrl = currentItem?.hostPageDisplayUrl
                        ImageDetails.thumbnailUrl = currentItem?.thumbnailUrl
                        imagesFragment.addImageDetailFragment()
                        ImageDetails.isImageDetailFragmentOpen = true
                    }
                } catch (e: Exception) {
                    Log.e("TAGINN1", e.stackTraceToString())
                }
            }


        } else {
            holder.thumbnailImage.visibility = View.GONE // Hide image view
        }
        //        holder.name.text = currentItem?.name
    }

    override fun getItemCount(): Int = searchItems?.size ?: 0
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //        name, displayUrl,datePublished,snippet
        // ... (reference other views in the item layout)
//        val isImageDetailVisible = false
        val thumbnailImage: ImageView = view.findViewById(R.id.searchedImage)
        fun bindImage(imageUrl: String?) {
            Log.d("TAGINN", imageUrl ?: "")
            Glide.with(itemView.context)
                .load(imageUrl)
                .into(thumbnailImage)
        }

    }

}
