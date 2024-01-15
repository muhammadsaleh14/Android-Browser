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
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.browserapp.R
import com.example.browserapp.adapters.ImageSearchAdapter
import com.example.browserapp.networkManagement.ConnectivityObserver
import com.example.browserapp.networkManagement.NetworkConnectivityObserver
import com.example.browserapp.viewmodels.ImagesViewModel
import com.example.browserapp.viewmodels.SearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ImagesFragment : Fragment(R.layout.fragment_images) {
    private lateinit var rvImageSearchResult: RecyclerView
    private val imagesViewModel: ImagesViewModel by activityViewModels()
    private lateinit var adapter: ImageSearchAdapter
    private var setAdapter = false
    private lateinit var searchViewModel: SearchViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchViewModel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]
        searchViewModel.isLoading.value = true
        // Inflate the layout and bind views
        val view = inflater.inflate(R.layout.fragment_images, container, false)
        rvImageSearchResult = view.findViewById(R.id.rvImageSearchResult)
        // ... (other view bindings)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            super.onViewCreated(view, savedInstanceState)
//            imagesViewModel.query = searchViewModel.searchTerm.value?:"error"
            searchViewModel.searchTerm.observe(this) { newData ->
                // Update your UI elements with the new data
                imagesViewModel.query = newData
                adapter.refresh()
            }
            val imagesLayoutManager =
                StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            rvImageSearchResult.layoutManager = imagesLayoutManager
            adapter = ImageSearchAdapter(ImageSearchAdapter.DIFF_CALLBACK, this)
            var connectivityObserver = NetworkConnectivityObserver(requireContext())
            lifecycleScope.launch {
                connectivityObserver.observe()
                    .collect { status ->
                        Log.d("qqq", "status: $status")
                        if (status == ConnectivityObserver.Status.Available) {
                            Log.d("qqq", "adapter refresh")
                            delay(2000)
                            adapter.refresh()
                        }
                    }
            }
            observePagingData()
            submitDataToAdapter()
        } catch (e: Exception) {
            Log.e("TAGINN1", e.stackTraceToString())
        }
    }

    private fun observePagingData() {
        try {
            adapter.addLoadStateListener { loadState ->
                if (loadState.refresh is LoadState.Loading) {
                    // Show loading indicator
                    searchViewModel.isLoading.value = true
                    setAdapter = false
                } else {
                    searchViewModel.isLoading.value = false
                    if (!setAdapter) {
                        rvImageSearchResult.adapter = adapter
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
                        Log.e("TAGINN5", "error occurred $error")
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
                imagesViewModel.flow.collect { pagingData ->
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

    fun addImageDetailFragment() {
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
