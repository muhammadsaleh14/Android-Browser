package com.example.browserapp.searchfragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.browserapp.R
import com.example.browserapp.adapters.VideosSearchAdapter
import com.example.browserapp.adapters.WebpagesSearchAdapter
import com.example.browserapp.viewmodels.VideosViewModel

class VideosFragment : Fragment(R.layout.fragment_videos) {
    private lateinit var rvVideosSearchResult: RecyclerView
    private val viewModel: VideosViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvVideosSearchResult = view.findViewById(R.id.rvVideosSearchResult)
        rvVideosSearchResult.layoutManager = LinearLayoutManager(context)
        rvVideosSearchResult.setHasFixedSize(true)
        observeSearchResults()

        viewModel.fetchVideoSearchResults()
    }

    private fun observeSearchResults() {
        viewModel.videosSearchValues.observe(viewLifecycleOwner) { videoSearchValues ->
            if (videoSearchValues != null) {
                rvVideosSearchResult.adapter = VideosSearchAdapter(videoSearchValues.value)
            } else {
                // Handle error cases
            }
        }
    }
}
