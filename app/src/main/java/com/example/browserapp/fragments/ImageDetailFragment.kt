package com.example.browserapp.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.browserapp.R
import com.example.browserapp.listeners.onImageCardClick
import com.example.browserapp.viewmodels.ImageDetails

class ImageDetailFragment : Fragment(R.layout.fragment_images_detail) {
    private lateinit var imageName: TextView
    private lateinit var hostPageDisplayUrl: TextView
    private lateinit var thumbnailImage: ImageView
    private lateinit var closeImageDetail: Button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            super.onViewCreated(view, savedInstanceState)
            imageName = view.findViewById(R.id.imageNameTextView)
            hostPageDisplayUrl = view.findViewById(R.id.hostPageDisplayUrlTextView)
            thumbnailImage = view.findViewById(R.id.imageViewDetails)
            closeImageDetail = view.findViewById(R.id.closeImageDetail)
            imageName.text = ImageDetails.name
            hostPageDisplayUrl.text = ImageDetails.hostPageDisplayUrl
            bindImage(view.context, ImageDetails.thumbnailUrl)

            onImageCardClick(hostPageDisplayUrl, view.context, ImageDetails.hostPageDisplayUrl,ImageDetails.name)
            val fragmentManager = requireActivity().supportFragmentManager

            fragmentManager.addOnBackStackChangedListener {
                if (fragmentManager.backStackEntryCount == 0) {
                    ImageDetails.isImageDetailFragmentOpen = false
                }
            }
//            ImageDetails.closeDetailFragment.observe(viewLifecycleOwner, Observer {
//                if (fragmentManager.backStackEntryCount > 0) {
//                    fragmentManager.popBackStack()
//                }
//            })
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
