package com.example.browserapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.browserapp.dataClasses.bingSearch.BingSearch
import com.example.browserapp.endpoint
import com.example.browserapp.getSearchWebResultAsync
import com.example.browserapp.searchTerm
import com.example.browserapp.subscriptionKey
import kotlinx.coroutines.launch

class WebPagesViewModel : ViewModel() {
    private val _webSearchValues = MutableLiveData<BingSearch?>()
    val webSearchValues: LiveData<BingSearch?> = _webSearchValues
    private var isDataFetched = false
    fun fetchWebSearchResults() {
        if (!isDataFetched){
            viewModelScope.launch {
                val result = getSearchWebResultAsync(searchTerm, subscriptionKey, endpoint)
                _webSearchValues.postValue(result) // Use postValue for main thread safety
                isDataFetched = true
            }
        }
    }
}
