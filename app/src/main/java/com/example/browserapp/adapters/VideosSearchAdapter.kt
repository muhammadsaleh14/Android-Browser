package com.example.browserapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.browserapp.R
import com.example.browserapp.dataClasses.bingSearch.VideosSearch
import com.example.browserapp.listeners.onVideoCardClick
import org.threeten.bp.Duration

class VideosSearchAdapter(diffCallback: DiffUtil.ItemCallback<VideosSearch.Value>) :
    PagingDataAdapter<VideosSearch.Value, VideosSearchAdapter.ViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the item layout dynamically
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_video_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val currentItem: VideosSearch.Value? = getItem(position)
            // Bind data to the views in the item layout
            if (currentItem?.thumbnailUrl != null) {
                holder.bindImage(currentItem.thumbnailUrl)
                holder.thumbnailImage.visibility = View.VISIBLE // Show image view
            } else {
                holder.thumbnailImage.visibility = View.GONE // Hide image view
            }
            val duration = Duration.parse(currentItem?.duration?:"PT0S")
            val formattedDuration = formatDuration(duration)
            holder.durationTextView.text = formattedDuration
            holder.publisherTextView.text = currentItem?.publisher?.get(0)?.name ?: ""
            holder.creatorNameTextView.text = currentItem?.creator?.name
            holder.nameTextView.text = currentItem?.name
            holder.descriptionTextView.text = currentItem?.description
            if (currentItem != null) {
                onVideoCardClick(
                    holder.videoClickableCard,
                    holder.itemView.context,
                    currentItem.hostPageUrl,
                    currentItem.name
                )
            }
        } catch (e: Exception) {
            Log.e("TAGINN3", e.stackTraceToString())
        }
        // ... (bind other views as needed)
    }

    private fun formatDuration(duration: Duration): String {
        val hours = duration.toHours().toInt()
        val minutes = duration.toMinutes().rem(60).toInt()
        val seconds = duration.seconds.rem(60).toInt()

        return "%02d:%02d:%02d".format(hours, minutes, seconds)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        //        name, displayUrl,datePublished,snippet
        // ... (reference other views in the item layout)
        // Get references to the views using findViewById()
        val videoClickableCard = view.findViewById<CardView>(R.id.videoCardView)
        val thumbnailImage: ImageView = view.findViewById(R.id.videoThumbnailImageView)
        val durationTextView: TextView = view.findViewById(R.id.videoDurationTextVeiw)
        val publisherTextView: TextView = view.findViewById(R.id.videoPublisherTextView)
        val creatorNameTextView: TextView = view.findViewById(R.id.videoCreatorNameTextView)
        val nameTextView: TextView = view.findViewById(R.id.videoNameTextView)
        val descriptionTextView: TextView = view.findViewById(R.id.videoDescriptionTextView)
        fun bindImage(imageUrl: String?) {
            Glide.with(itemView.context)
                .load(imageUrl)
                .into(thumbnailImage)
        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<VideosSearch.Value>() {
            override fun areItemsTheSame(
                oldItem: VideosSearch.Value,
                newItem: VideosSearch.Value
            ): Boolean {
                try {
                    // Return true if items represent the same web page
                    val bool = oldItem.videoId == newItem.videoId
                    Log.d("TAGINN2", "are items the same:$bool")
                    return bool
                } catch (e: Exception) {
                    Log.e("TAGINN3", e.stackTraceToString())
                    throw e
                }
            }

            override fun areContentsTheSame(
                oldItem: VideosSearch.Value,
                newItem: VideosSearch.Value
            ): Boolean {
                try {
                    // Return true if items have the same content (name, url, etc.)
                    val bool = oldItem == newItem
                    Log.d("TAGINN2", "are contents the same:$bool")
                    return bool
                } catch (e: Exception) {
                    Log.e("TAGINN3", e.stackTraceToString())
                    throw e
                }
            }
        }
    }
}
