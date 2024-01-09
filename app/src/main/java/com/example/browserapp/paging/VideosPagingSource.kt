package com.example.browserapp.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bumptech.glide.load.HttpException
import com.example.browserapp.dataClasses.bingSearch.VideosSearch
import com.example.browserapp.dataClasses.bingSearch.WebpagesSearch
import com.example.browserapp.search.getSearchVideosResultAsync
import com.example.browserapp.search.getSearchWebResultAsync
import com.example.browserapp.search.videosCount
import java.io.IOException

class VideosPagingSource(
    val query: String? = ""
) : PagingSource<Int, VideosSearch.Value>() {
    override suspend fun load(params: LoadParams<Int>):
            LoadResult<Int, VideosSearch.Value> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 0
            Log.d("TAGINN3", "next page number $nextPageNumber")
            val response = getSearchVideosResultAsync(query ?: "", nextPageNumber)
            val data = response?.value?.filterNotNull() ?: emptyList()

            Log.d("TAGINN3", "${response?.value}")
            val nextKey = if ((response?.totalEstimatedMatches != null) &&
                ((response.totalEstimatedMatches - videosCount) > nextPageNumber)
            ) {
                response.nextOffset
            } else {
                null
            }
            Log.d("TAGINN3", "nextKey is $nextKey")

            return LoadResult.Page(
                data = data,
                prevKey = null, // Only paging forward.
                nextKey = nextKey
            )
        } catch (e: IOException) {
            // IOException for network failures.
            Log.e("TAGINN3", e.stackTraceToString())
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            Log.e("TAGINN3", e.stackTraceToString())
            // HttpException for any non-2xx HTTP status codes.
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, VideosSearch.Value>): Int? {
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