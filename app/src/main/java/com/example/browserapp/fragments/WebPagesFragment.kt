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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.browserapp.R
import com.example.browserapp.activities.SearchActivity
import com.example.browserapp.adapters.WebpagesSearchAdapter
import com.example.browserapp.networkManagement.ConnectivityObserver
import com.example.browserapp.networkManagement.NetworkConnectivityObserver
import com.example.browserapp.viewmodels.SearchViewModel
import com.example.browserapp.viewmodels.WebPagesViewModel
import kotlinx.coroutines.launch


class WebPagesFragment() : Fragment(R.layout.fragment_web_pages) {
    private lateinit var rvSearchResult: RecyclerView
    private val viewModel: WebPagesViewModel by activityViewModels()
    private lateinit var adapter: WebpagesSearchAdapter
    private var setAdapter = false
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var fragmentManager: FragmentManager
    lateinit var swipeRefreshWebpages: SwipeRefreshLayout
    private var isRefreshAdapter = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchViewModel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]
        // Inflate the layout and bind views
        val view = inflater.inflate(R.layout.fragment_web_pages, container, false)
        rvSearchResult = view.findViewById(R.id.rvWebpagesSearchResult)
        swipeRefreshWebpages = view.findViewById(R.id.swipeRefreshWebpages)
        fragmentManager = requireActivity().supportFragmentManager
        // ... (other view bindings)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setting search value
//        viewModel.query = searchViewModel.searchTerm.value ?: "error"
        //so that adapter doesnt refresh every time fragment is focused
        searchViewModel.searchTerm.observe(viewLifecycleOwner) { newData ->
            Log.d("observer","value $newData loaded ${searchViewModel.webPagesLoaded}")
            // Update your UI elements with the new data
            if (!searchViewModel.webPagesLoaded) {
                viewModel.query = newData
                adapter.refresh()
                searchViewModel.webPagesLoaded = true
            }
        }
        //setting loading icon
        searchViewModel.isLoading.value = true
        //setting recycler view
        rvSearchResult.layoutManager = LinearLayoutManager(context)
        rvSearchResult.setHasFixedSize(true)

        swipeRefreshWebpages.setOnRefreshListener {
            // Perform your refresh actions here
            Log.d("ssss", "swipe refresh")
            adapter.refresh()
        }
        //initialising adapter after
        adapter = WebpagesSearchAdapter(WebpagesSearchAdapter.DIFF_CALLBACK)
        observePagingData()
        submitDataToAdapter()
    }

    private fun observePagingData() {
        Log.d("TAGINN2", "observe PAGING DATA ftn")

        adapter.addLoadStateListener { loadState ->
            Log.d("sss", "swipe loading to false")
            swipeRefreshWebpages.isRefreshing = false
            if (loadState.refresh is LoadState.Loading) {
                Log.d("qqq", "loading")
                searchViewModel.isLoading.value = true
                setAdapter = false
            } else {
                val connectivityObserver = NetworkConnectivityObserver(requireContext())
                val status = connectivityObserver.getCurrentStatus()
                if (status != ConnectivityObserver.Status.Available) {
                    SearchActivity.showAlert(status,requireView())
                }
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
