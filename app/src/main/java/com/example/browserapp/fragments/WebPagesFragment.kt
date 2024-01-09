package com.example.browserapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.coroutines.launch

class WebPagesFragment : Fragment(R.layout.fragment_web_pages) {
    private lateinit var rvSearchResult: RecyclerView
    private val viewModel: WebPagesViewModel by activityViewModels()
    private lateinit var adapter: WebpagesSearchAdapter
    private var setAdapter = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout and bind views
        val view = inflater.inflate(R.layout.fragment_web_pages, container, false)
        rvSearchResult = view.findViewById(R.id.rvWebpagesSearchResult)
        // ... (other view bindings)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.query = searchTerm

        rvSearchResult.layoutManager = LinearLayoutManager(context)
        rvSearchResult.setHasFixedSize(true)

        adapter = WebpagesSearchAdapter(WebpagesSearchAdapter.DIFF_CALLBACK)
        observePagingData()
        submitDataToAdapter()

        Log.d("TAGINN2", "calling onViewCreated")
    }

    private fun observePagingData() {
        Log.d("TAGINN2", "observe PAGING DATA ftn")

        adapter.addLoadStateListener { loadState ->
            // Handle loading and error states
            Log.d("TAGINN2", "observe")
            if (loadState.refresh is LoadState.Loading) {
                Log.d("TAGINN2", "loading")
                // Show loading indicator
            } else {
                if (!setAdapter) {
                    Log.d("TAGINN4", "setting adapter to true")
                    rvSearchResult.adapter = adapter
                    setAdapter = true
                }
//                viewLifecycleOwner.lifecycleScope.launch {
//                    viewModel.flow.collect{ pagingData ->
//                        Log.d("TAGINN2", "submitting data to adapter ${pagingData.toString()}")
//                        adapter.submitData(pagingData)
//                    }
//                }
                Log.d("TAGINN2", "not Loading")
                // Hide loading indicator
                val error = when {
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }

                error?.let {
                    Log.e("TAGINN2", "error occurred $error")
                    // Handle error
                }
            }
        }
    }

    private fun submitDataToAdapter() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.flow.collect { pagingData ->
                Log.d("TAGINN2", "submitting data to adapter ${pagingData.toString()}")

                adapter.submitData(pagingData)
            }
        }
    }
}
