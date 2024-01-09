package com.example.browserapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.browserapp.dataClasses.bingSearch.WebpagesSearch
import com.example.browserapp.paging.WebpagesPagingSource
import com.example.browserapp.search.webSearchEndpoint
import com.example.browserapp.search.getSearchWebResultAsync
import com.example.browserapp.search.searchTerm
import com.example.browserapp.search.subscriptionKey
import com.example.browserapp.search.webpagesCount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class WebPagesViewModel : ViewModel() {
    var query:String = "error"
    private val _webSearchValues = MutableLiveData<WebpagesSearch?>()
//    val webSearchValues: LiveData<WebpagesSearch?> = _webSearchValues
//    private var isDataFetched = false
    val flow: Flow<PagingData<WebpagesSearch.WebPages.Value>> = Pager(
        PagingConfig(pageSize = webpagesCount, prefetchDistance = 5 ) // Example configuration
    ) {
        WebpagesPagingSource(query)
    }.flow
        .cachedIn(viewModelScope)
//    fun fetchWebSearchResults(query:String) {
//
//        if (!isDataFetched){
//            viewModelScope.launch {
//                val result = getSearchWebResultAsync(query,0)
//                _webSearchValues.postValue(result) // Use postValue for main thread safety
//                if(result !== null){
//                    isDataFetched = true
//                }
//            }
//        }
//    }
}
