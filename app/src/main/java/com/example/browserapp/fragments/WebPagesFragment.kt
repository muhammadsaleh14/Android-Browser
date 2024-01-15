package com.example.browserapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.browserapp.R
import com.example.browserapp.adapters.WebpagesSearchAdapter
import com.example.browserapp.networkManagement.ConnectivityObserver
import com.example.browserapp.networkManagement.NetworkConnectivityObserver
import com.example.browserapp.viewmodels.SearchViewModel
import com.example.browserapp.viewmodels.WebPagesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WebPagesFragment() : Fragment(R.layout.fragment_web_pages) {
    private lateinit var rvSearchResult: RecyclerView
    private val viewModel: WebPagesViewModel by activityViewModels()
    private lateinit var adapter: WebpagesSearchAdapter
    private var setAdapter = false
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var fragmentManager: FragmentManager
    private var isRefreshAdapter = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchViewModel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]
        // Inflate the layout and bind views
        val view = inflater.inflate(R.layout.fragment_web_pages, container, false)
        rvSearchResult = view.findViewById(R.id.rvWebpagesSearchResult)
        fragmentManager = requireActivity().supportFragmentManager
        // ... (other view bindings)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setting search value
//        viewModel.query = searchViewModel.searchTerm.value ?: "error"
        searchViewModel.searchTerm.observe(this) { newData ->
            // Update your UI elements with the new data
            viewModel.query = newData
            adapter.refresh()
        }
        //setting loading icon
        searchViewModel.isLoading.value = true
        //setting recycler view
        rvSearchResult.layoutManager = LinearLayoutManager(context)
        rvSearchResult.setHasFixedSize(true)
        //recycler view adapter
        //handling connectivity
        var connectivityObserver = NetworkConnectivityObserver(requireContext())
        lifecycleScope.launch {
            connectivityObserver.observe()
                .collect { status ->
                    Log.d("qqq", "status: $status")
                    if (status == ConnectivityObserver.Status.Available) {
                        Log.d("qqq", "adapter refresh")
                        delay(2000)
                        try {
                            adapter.refresh()
                        } catch (e: UninitializedPropertyAccessException) {
                            Log.e("qqq", "adapter is no yet initialised")
                        }
                    }
                    Log.e("qqq", "setting first loaded to true")
                }
        }
        //initialising adapter after
        adapter = WebpagesSearchAdapter(WebpagesSearchAdapter.DIFF_CALLBACK)
        observePagingData()
        submitDataToAdapter()
    }

    private fun observePagingData() {
        Log.d("TAGINN2", "observe PAGING DATA ftn")

        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                Log.d("qqq", "loading")
                searchViewModel.isLoading.value = true
                setAdapter = false
            } else {
                Log.d("qqq", "not loading")
                searchViewModel.isLoading.value = false
                if (!setAdapter) {
                    Log.d("TAGINN5", "setting adapter to true")
                    rvSearchResult.adapter = adapter
                    setAdapter = true
                }
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

    private fun reattachFragment() {
        fragmentManager.beginTransaction()
            .detach(this)
            .attach(this)
            .commit()
    }
}
