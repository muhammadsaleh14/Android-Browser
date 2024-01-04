package com.example.browserapp.searchfragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.browserapp.R
import com.example.browserapp.adapters.ImageSearchAdapter
import com.example.browserapp.adapters.SearchAdapter
import com.example.browserapp.viewmodels.ImagesViewModel

class ImagesFragment : Fragment(R.layout.fragment_images) {
    private lateinit var rvImageSearchResult: RecyclerView
    private val viewModel: ImagesViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvImageSearchResult = view.findViewById(R.id.rvImageSearchResult)
        val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)

        rvImageSearchResult.layoutManager = layoutManager
        rvImageSearchResult.setHasFixedSize(true)


        observeSearchResults()

        viewModel.fetchImagesSearchResult()
    }

    private fun observeSearchResults() {
        viewModel.imagesSearchValues.observe(viewLifecycleOwner) { imageSearchValues ->
            if (imageSearchValues != null) {
                rvImageSearchResult.adapter = ImageSearchAdapter(imageSearchValues.value)
            } else {
                // Handle error cases
            }
        }
    }
}
