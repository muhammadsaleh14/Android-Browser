package com.example.browserapp.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.browserapp.R
import com.example.browserapp.adapters.ImageSearchAdapter
import com.example.browserapp.viewmodels.ImagesViewModel


class ImagesFragment : Fragment(R.layout.fragment_images) {
    private lateinit var rvImageSearchResult: RecyclerView
    private val viewModel: ImagesViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            super.onViewCreated(view, savedInstanceState)

            rvImageSearchResult = view.findViewById(R.id.rvImageSearchResult)
            val imagesLayoutManager =  StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            rvImageSearchResult.layoutManager = imagesLayoutManager

            observeSearchResults(view.context)
            viewModel.fetchImagesSearchResult()
        } catch (e: Exception) {
            Log.e("TAGINN1", e.stackTraceToString())
        }
    }

    private fun observeSearchResults(context: Context) {
        viewModel.imagesSearchValues.observe(viewLifecycleOwner) { imageSearchValues ->
            if (imageSearchValues != null) {
                rvImageSearchResult.adapter = ImageSearchAdapter(this, imageSearchValues.value)
            } else {
                // Handle error cases
            }
        }
    }
    fun addImageDetailFragment(){
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.slide_in_from_left, // Enter animation for the new fragment
            R.anim.slide_out_to_left, // Exit animation for the current fragment
            R.anim.slide_in_from_left, // Enter animation for the previous fragment (pop back stack)
            R.anim.slide_out_to_left // Exit animation for the current fragment (pop back stack)
        )
        val imageDetailFragment = ImageDetailFragment()
        fragmentTransaction.add(R.id.fragmentContainer, imageDetailFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

    }
}
