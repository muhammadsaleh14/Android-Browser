package com.example.browserapp.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.browserapp.dataClasses.bingSearch.VideosSearch
import com.example.browserapp.search.getSearchVideosResultAsync
import com.example.browserapp.search.webSearchEndpoint
import com.example.browserapp.search.getSearchWebResultAsync
import com.example.browserapp.search.searchTerm
import com.example.browserapp.search.subscriptionKey
import com.example.browserapp.search.videosSearchEndpoint
import kotlinx.coroutines.launch

class VideosViewModel : ViewModel() {
    private val _VideosSearchValues = MutableLiveData<VideosSearch?>()
    val videosSearchValues: LiveData<VideosSearch?> = _VideosSearchValues
    private var isDataFetched = false
    fun fetchVideoSearchResults() {
        if (!isDataFetched){
            viewModelScope.launch {
                val result = getSearchVideosResultAsync(searchTerm, subscriptionKey, videosSearchEndpoint)
                _VideosSearchValues.postValue(result) // Use postValue for main thread safety
                if(result !== null){
                    isDataFetched = true
                }
            }
        }
    }
}
