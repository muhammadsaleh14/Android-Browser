package com.example.browserapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.browserapp.dataClasses.bingSearch.WebpagesSearch
import com.example.browserapp.search.webSearchEndpoint
import com.example.browserapp.search.getSearchWebResultAsync
import com.example.browserapp.search.searchTerm
import com.example.browserapp.search.subscriptionKey
import kotlinx.coroutines.launch

class WebPagesViewModel : ViewModel() {
    private val _webSearchValues = MutableLiveData<WebpagesSearch?>()
    val webSearchValues: LiveData<WebpagesSearch?> = _webSearchValues
    private var isDataFetched = false
    fun fetchWebSearchResults() {
        if (!isDataFetched){
            viewModelScope.launch {
                val result = getSearchWebResultAsync(searchTerm, subscriptionKey, webSearchEndpoint)
                _webSearchValues.postValue(result) // Use postValue for main thread safety
                if(result !== null){
                    isDataFetched = true
                }
            }
        }
    }
}
