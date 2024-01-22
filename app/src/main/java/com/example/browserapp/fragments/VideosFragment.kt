package com.example.browserapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.browserapp.R
import com.example.browserapp.activities.SearchActivity
import com.example.browserapp.adapters.VideosSearchAdapter
import com.example.browserapp.networkManagement.ConnectivityObserver
import com.example.browserapp.networkManagement.NetworkConnectivityObserver
import com.example.browserapp.search.searchTerm
import com.example.browserapp.viewmodels.SearchViewModel
import com.example.browserapp.viewmodels.VideosViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VideosFragment : Fragment(R.layout.fragment_videos) {
    private lateinit var rvVideosSearchResult: RecyclerView
    private val viewModel: VideosViewModel by activityViewModels()
    private lateinit var adapter: VideosSearchAdapter
    private var setAdapter = false
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var swipeRefreshVideos: SwipeRefreshLayout
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {

            searchViewModel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]
            searchViewModel.isLoading.value = true
            super.onViewCreated(view, savedInstanceState)
//            viewModel.query = searchViewModel.searchTerm.value?:"error"
            searchViewModel.searchTerm.observe(viewLifecycleOwner) { newData ->
                Log.d("observer","value $newData loaded ${searchViewModel.videosLoaded}")
              if (!searchViewModel.videosLoaded) {
                    viewModel.query = newData
                    adapter.refresh()
                    searchViewModel.videosLoaded = true
                }
            }
            swipeRefreshVideos = view.findViewById(R.id.swipeRefreshVideos)
            rvVideosSearchResult = view.findViewById(R.id.rvVideosSearchResult)
            rvVideosSearchResult.layoutManager = LinearLayoutManager(context)
            rvVideosSearchResult.setHasFixedSize(true)
            adapter = VideosSearchAdapter(VideosSearchAdapter.DIFF_CALLBACK)
            swipeRefreshVideos.setOnRefreshListener {
                // Perform your refresh actions here
                adapter.refresh()
            }
            observePagingData()
            submitDataToAdapter()
        } catch (e: Exception) {
            Log.e("TAGINN3", e.stackTraceToString())
        }
    }

    private fun observePagingData() {
        try {
            adapter.addLoadStateListener { loadState ->
                swipeRefreshVideos.isRefreshing = false
                if (loadState.refresh is LoadState.Loading) {
                    // Show loading indicator
                    searchViewModel.isLoading.value = true
                    setAdapter = false
                } else {
                    val connectivityObserver = NetworkConnectivityObserver(requireContext())
                    val status = connectivityObserver.getCurrentStatus()
                    if (status != ConnectivityObserver.Status.Available) {
                        SearchActivity.showAlert(status,requireView())
                    }
                    searchViewModel.isLoading.value = false
                    if (!setAdapter) {
                        rvVideosSearchResult.adapter = adapter
                        setAdapter = true
                    }
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
