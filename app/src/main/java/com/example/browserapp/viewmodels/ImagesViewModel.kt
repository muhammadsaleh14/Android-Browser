package com.example.browserapp.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.browserapp.dataClasses.bingSearch.ImagesSearch
import com.example.browserapp.paging.ImagesPagingSource
import com.example.browserapp.search.imagesCount
import kotlinx.coroutines.flow.Flow

class ImagesViewModel : ViewModel() {
    var query:String = "error"
    //    private val _webSearchValues = MutableLiveData<WebpagesSearch?>()
    //    val webSearchValues: LiveData<WebpagesSearch?> = _webSearchValues
//    private var isDataFetched = false
    val flow: Flow<PagingData<ImagesSearch.Value>> = Pager(
        PagingConfig(pageSize = imagesCount, prefetchDistance = 20) // Example configuration
    ) {
        ImagesPagingSource(query)
    }.flow
        .cachedIn(viewModelScope)
}
