package com.example.browserapp.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.browserapp.R
import com.example.browserapp.viewmodels.ImageDetails

class ImageDetailFragment : Fragment(R.layout.fragment_images_detail) {
    private lateinit var imageName: TextView
    private lateinit var imageContentUrl: TextView
    private lateinit var thumbnailImage: ImageView
    private lateinit var closeImageDetail: Button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            super.onViewCreated(view, savedInstanceState)
            imageName = view.findViewById(R.id.imageNameTextView)
            imageContentUrl = view.findViewById(R.id.imageContentUrlTextView)
            thumbnailImage = view.findViewById(R.id.imageViewDetails)
            closeImageDetail = view.findViewById(R.id.closeImageDetail)
            imageName.text = ImageDetails.name
            imageContentUrl.text = ImageDetails.contentUrl
            bindImage(view.context, ImageDetails.thumbnailUrl)
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.addOnBackStackChangedListener {
                if (fragmentManager.backStackEntryCount == 0) {
                    ImageDetails.isImageDetailFragmentOpen = false
                }
            }
            closeImageDetail.setOnClickListener {
                fragmentManager.popBackStack()
            }

        } catch (e: Exception) {
            Log.e("TAGINN1", e.stackTraceToString())
        }
    }

    private fun bindImage(context: Context, imageUrl: String?) {
        Log.d("TAGINN1", imageUrl ?: "")
        Glide.with(context)
            .load(imageUrl)
            .into(thumbnailImage)
    }

}
