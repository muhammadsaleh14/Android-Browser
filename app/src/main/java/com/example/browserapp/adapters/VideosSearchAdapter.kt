package com.example.browserapp.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.browserapp.R
import com.example.browserapp.dataClasses.bingSearch.VideosSearch
import org.threeten.bp.Duration
import org.threeten.bp.format.DateTimeFormatter
import com.example.browserapp.dataClasses.bingSearch.WebpagesSearch
import com.example.browserapp.listeners.onVideoCardClick

class VideosSearchAdapter(private val context: Context, private val searchItems: List<VideosSearch.Value?>?) :
    RecyclerView.Adapter<VideosSearchAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the item layout dynamically
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_video_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem:VideosSearch.Value? = searchItems?.get(position)
        // Bind data to the views in the item layout
        if (currentItem?.thumbnailUrl != null) {
            holder.bindImage(currentItem.thumbnailUrl)
            holder.thumbnailImage.visibility = View.VISIBLE // Show image view
        } else {
            holder.thumbnailImage.visibility = View.GONE // Hide image view
        }

        val duration = Duration.parse(currentItem?.duration)
        val formattedDuration = formatDuration(duration)
        holder.durationTextView.text = formattedDuration
        holder.publisherTextView.text = currentItem?.publisher?.get(0)?.name ?: ""
        holder.creatorNameTextView.text = currentItem?.creator?.name
        holder.nameTextView.text = currentItem?.name
        holder.descriptionTextView.text = currentItem?.description
        onVideoCardClick(holder.videoClickableCard,context,currentItem?.hostPageUrl)
        // ... (bind other views as needed)
    }
    private fun formatDuration(duration: Duration): String {
        val hours = duration.toHours().toInt()
        val minutes = duration.toMinutes().rem(60).toInt()
        val seconds = duration.seconds.rem(60).toInt()

        return "%02d:%02d:%02d".format(hours, minutes, seconds)
    }
    override fun getItemCount(): Int = searchItems?.size ?: 0
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
}
