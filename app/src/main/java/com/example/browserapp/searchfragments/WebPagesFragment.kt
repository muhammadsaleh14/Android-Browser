package com.example.browserapp.searchfragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.browserapp.R
import com.example.browserapp.adapters.SearchAdapter
import com.example.browserapp.viewmodels.WebPagesViewModel

class WebPagesFragment : Fragment(R.layout.fragment_web_pages) {
    private lateinit var rvSearchResult: RecyclerView
    private val viewModel: WebPagesViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvSearchResult = view.findViewById(R.id.rvSearchResult)
        rvSearchResult.layoutManager = LinearLayoutManager(context)
        rvSearchResult.setHasFixedSize(true)

        observeSearchResults()

        viewModel.fetchWebSearchResults()
    }

    private fun observeSearchResults() {
        viewModel.webSearchValues.observe(viewLifecycleOwner) { webSearchValues ->
            if (webSearchValues != null) {
                rvSearchResult.adapter = SearchAdapter(webSearchValues.webPages?.value)
            } else {
                // Handle error cases
            }
        }
    }
}
