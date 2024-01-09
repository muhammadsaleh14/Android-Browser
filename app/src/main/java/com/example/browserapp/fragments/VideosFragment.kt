package com.example.browserapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.browserapp.R
import com.example.browserapp.adapters.VideosSearchAdapter
import com.example.browserapp.search.searchTerm
import com.example.browserapp.viewmodels.VideosViewModel
import kotlinx.coroutines.launch

class VideosFragment : Fragment(R.layout.fragment_videos) {
    private lateinit var rvVideosSearchResult: RecyclerView
    private val viewModel: VideosViewModel by activityViewModels()
    private lateinit var adapter: VideosSearchAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            super.onViewCreated(view, savedInstanceState)
            viewModel.query = searchTerm
            rvVideosSearchResult = view.findViewById(R.id.rvVideosSearchResult)
            rvVideosSearchResult.layoutManager = LinearLayoutManager(context)
            rvVideosSearchResult.setHasFixedSize(true)
            adapter = VideosSearchAdapter(VideosSearchAdapter.DIFF_CALLBACK)
            observePagingData()
            submitDataToAdapter()
        } catch (e: Exception) {
            Log.e("TAGINN3", e.stackTraceToString())
        }
    }

    private fun observePagingData() {
        try {
            adapter.addLoadStateListener { loadState ->
                if (loadState.refresh is LoadState.Loading) {
                    // Show loading indicator
                } else {
                    rvVideosSearchResult.adapter = adapter
                    // Hide loading indicator
                    val error = when {
                        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                        else -> null
                    }

                    error?.let {
                        Log.e("TAGINN3", "error occurred $error")
                        // Handle error
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("TAGINN3", e.stackTraceToString())
        }
    }

    private fun submitDataToAdapter() {
        Log.d("TAGINN3", "submitting data to adapter")
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                viewModel.flow.collect { pagingData ->
                    try {
                        Log.e("TAGINN3", "submitting data to adapter ${pagingData.toString()}")
                        adapter.submitData(pagingData)
                    } catch (e: Exception) {
                        Log.e("TAGINN3", e.stackTraceToString())
                    }
                }
            } catch (e: Exception) {
                Log.e("TAGINN3", e.stackTraceToString())
            }
        }
    }
}
