package com.example.browserapp.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.browserapp.R
import com.example.browserapp.viewmodels.ImageDetails

class ImageDetailFragment : Fragment(R.layout.fragment_images) {
    private lateinit var imageCardView: CardView
    private lateinit var imageName: TextView
    private lateinit var imageContentUrl: TextView
    private lateinit var thumbnailImage:ImageView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            super.onViewCreated(view, savedInstanceState)
            imageCardView = view.findViewById(R.id.imageCardView)
            imageName = view.findViewById(R.id.imageNameTextView)
            imageContentUrl = view.findViewById(R.id.imageContentUrlTextView)
            thumbnailImage = view.findViewById(R.id.imageViewDetails)

            imageName.text = ImageDetails.name
            imageContentUrl.text = ImageDetails.contentUrl
            bindImage(view.context,ImageDetails.thumbnailUrl)





        } catch (e: Exception) {
            Log.e("TAGINN1", e.stackTraceToString())
        }
    }
    private fun bindImage(context:Context, imageUrl: String?) {
        Log.d("TAGINN", imageUrl ?: "")
        Glide.with(context)
            .load(imageUrl)
            .into(thumbnailImage)
    }

}
