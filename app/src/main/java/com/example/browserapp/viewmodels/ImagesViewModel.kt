package com.example.browserapp.viewmodels

import com.example.browserapp.dataClasses.bingSearch.ImagesSearch
import com.example.browserapp.search.getImagesSearchResultAsync

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.browserapp.search.webSearchEndpoint
import com.example.browserapp.search.searchTerm
import com.example.browserapp.search.subscriptionKey
import kotlinx.coroutines.launch

class ImagesViewModel : ViewModel() {
    private val _imagesSearchValues = MutableLiveData<ImagesSearch?>()
    val imagesSearchValues: LiveData<ImagesSearch?> = _imagesSearchValues
    private var isDataFetched = false
    fun fetchImagesSearchResult() {
        if (!isDataFetched){
            viewModelScope.launch {
                val result = getImagesSearchResultAsync(searchTerm, subscriptionKey, webSearchEndpoint)
                _imagesSearchValues.postValue(result) // Use postValue for main thread safety
                if(result !== null){
                    isDataFetched = true
                }
            }
        }
    }
}