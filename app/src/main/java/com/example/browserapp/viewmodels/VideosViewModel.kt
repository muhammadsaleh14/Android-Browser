package com.example.browserapp.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.browserapp.dataClasses.bingSearch.VideosSearch
import com.example.browserapp.paging.VideosPagingSource
import com.example.browserapp.search.getSearchWebResultAsync
import com.example.browserapp.search.searchTerm
import com.example.browserapp.search.subscriptionKey
import com.example.browserapp.search.videosCount
import com.example.browserapp.search.videosSearchEndpoint
import com.example.browserapp.search.webpagesCount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class VideosViewModel : ViewModel() {
    var query:String = "error"
    //    private val _webSearchValues = MutableLiveData<WebpagesSearch?>()
    //    val webSearchValues: LiveData<WebpagesSearch?> = _webSearchValues
//    private var isDataFetched = false
    val flow: Flow<PagingData<VideosSearch.Value>> = Pager(
        PagingConfig(pageSize = videosCount, prefetchDistance = 5) // Example configuration
    ) {
        VideosPagingSource(query)
    }.flow
        .cachedIn(viewModelScope)
}
