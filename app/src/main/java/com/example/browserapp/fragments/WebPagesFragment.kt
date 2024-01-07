package com.example.browserapp.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.browserapp.R
import com.example.browserapp.adapters.WebpagesSearchAdapter
import com.example.browserapp.search.searchTerm
import com.example.browserapp.viewmodels.WebPagesViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WebPagesFragment : Fragment(R.layout.fragment_web_pages) {
    private lateinit var rvSearchResult: RecyclerView
    private val viewModel: WebPagesViewModel by activityViewModels()
    private lateinit var adapter: WebpagesSearchAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.query = searchTerm
        rvSearchResult = view.findViewById(R.id.rvWebpagesSearchResult)
        rvSearchResult.layoutManager = LinearLayoutManager(context)
        rvSearchResult.setHasFixedSize(true)

        adapter = WebpagesSearchAdapter(WebpagesSearchAdapter.DIFF_CALLBACK) // Use DiffUtil callback
        rvSearchResult.adapter = adapter

        observePagingData()

        viewModel.fetchWebSearchResults(searchTerm) // Trigger initial loading
    }

    private fun observePagingData() {
        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.flow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }

        adapter.addLoadStateListener { loadState ->
            // Handle loading and error states
            if (loadState.refresh is LoadState.Loading) {
                // Show loading indicator
            } else {
                // Hide loading indicator
                val error = when {
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                error?.let {
                    // Handle error
                }
            }
        }
    }
}
