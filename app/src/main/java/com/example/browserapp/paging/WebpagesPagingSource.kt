package com.example.browserapp.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bumptech.glide.load.HttpException
import com.example.browserapp.dataClasses.bingSearch.WebpagesSearch
import com.example.browserapp.search.getSearchWebResultAsync
import com.example.browserapp.search.webpagesCount
import java.io.IOException

class WebpagesPagingSource(private var query: String) : PagingSource<Int, WebpagesSearch.WebPages.Value>() {
    override suspend fun load(params: LoadParams<Int>):
            LoadResult<Int, WebpagesSearch.WebPages.Value> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 0
            Log.d("qqq", "next page number $nextPageNumber")
            query = query?:"cricket"

            val response = getSearchWebResultAsync(query, nextPageNumber)
            val data = response?.webPages?.value?.filterNotNull() ?: emptyList()
            val nextKey = if ((response?.webPages?.totalEstimatedMatches != null) &&
                ((response.webPages.totalEstimatedMatches - webpagesCount) > nextPageNumber)
            ) {
                nextPageNumber + webpagesCount
            } else {
                null
            }
            Log.d("qqq", "response is $data")
            return LoadResult.Page(
                data = data,
                prevKey = null, // Only paging forward.
                nextKey = nextKey
            )
        } catch (e: IOException) {
            // IOException for network failures.
            Log.e("TAGINN2", e.stackTraceToString())
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            Log.e("TAGINN2", e.stackTraceToString())
            // HttpException for any non-2xx HTTP status codes.
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, WebpagesSearch.WebPages.Value>): Int? {
//         Try to find the page key of the closest page to anchorPosition from
//         either the prevKey or the nextKey; you need to handle nullability
//         here.
//          * prevKey == null -> anchorPage is the first page.
//          * nextKey == null -> anchorPage is the last page.
//          * both prevKey and nextKey are null -> anchorPage is the
//            initial page, so return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    }
}