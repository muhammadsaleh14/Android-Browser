package com.example.browserapp.search

import android.util.Log
import com.example.browserapp.dataClasses.bingSearch.VideosSearch
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection

const val videosSearchEndpoint: String = "https://api.bing.microsoft.com/v7.0/videos/search"
fun main() {
//    runBlocking {
//        getSearchVideosResultAsync("gol gappay", subscriptionKey, videosSearchEndpoint)
//    }
//    val results = searchVideos(searchTerm, nextPageNumber)
//    print(results.jsonResponse)
}

suspend fun getSearchVideosResultAsync(
    searchText: String,
    nextPageNumber: Int
): VideosSearch? = withContext(Dispatchers.IO) {
    try {

//        Log.d("TAGINN3","Fetching results")
        val results = searchVideos(searchText,nextPageNumber)
//        Log.d("TAGINN3","response in ftn $results.jsonResponse")
        return@withContext parseVideosResponse(results.jsonResponse)
    } catch (e: Exception) {
        Log.e("TAGINN3", "catch getSearchVideosResult ${e.stackTraceToString() ?: ""}")
        return@withContext null
    }
}

fun searchVideos(searchQuery: String, nextPageNumber: Int): SearchResults {
    try {

        val safeSearchValue = URLEncoder.encode("Moderate", "UTF-8")
        val setLang = URLEncoder.encode("en", "UTF-8")
        //        val answerCount = 1
//        val responseFilter = URLEncoder.encode("webpages", "UTF-8")

        val urlString = "$videosSearchEndpoint?q=${URLEncoder.encode(searchQuery, "UTF-8")}" +
                "&safeSearch=${safeSearchValue}" +
                "&count=$videosCount" +
                "&offset=$nextPageNumber"+
                "&setLang=$setLang"
//                "&answerCount=$answerCount"+
//                "&responseFilter=$responseFilter"
        val url = URL(urlString)
//        Log.d("TAGINN3","url $urlString")
        val connection = url.openConnection() as HttpsURLConnection
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey )
//        val clientIP = InetAddress.getLocalHost().hostAddress
//        connection.setRequestProperty("X-MSEdge-ClientIP", clientIP)
        val response = connection.inputStream.bufferedReader().use { it.readText() }
        val results = SearchResults(HashMap(), response)
        connection.headerFields.forEach { (header, values) ->
            if (header != null && (header.startsWith("BingAPIs-") || header.startsWith("X-MSEdge-"))) {
                results.relevantHeaders[header] = values[0]
            }
        }
        return results
    } catch (e: Exception) {
        print(e.printStackTrace())
        Log.e("TAGINN", "catch searchVideos ${e.stackTraceToString() ?: ""}")
        return SearchResults(HashMap(), "")
    }
}

fun parseVideosResponse(jsonText: String): VideosSearch {
    try {
        val parser = JsonParser()
        val json = parser.parse(jsonText) as JsonObject
        print(json)
        val gson = GsonBuilder().setPrettyPrinting().create()
        val prettyJson = gson.toJson(json)
        return gson.fromJson(prettyJson, VideosSearch::class.java)
    } catch (e: Exception) {
        throw e
    }
}



