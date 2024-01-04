package com.example.browserapp.search

import android.util.Log
import com.example.browserapp.dataClasses.bingSearch.ImagesSearch
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection

const val imagesEndpoint: String = "https://api.bing.microsoft.com/v7.0/images/search"

//fun main() {
////    runBlocking {
////        getSearchWebResultAsync("multan videos", subscriptionKey, imagesEndpoint)
////    }
//    val results = searchImages("gol gappay")
//    print(results.jsonResponse)
//}

suspend fun getImagesSearchResultAsync(
    searchText: String,
    subscriptionKey : String?,
    imagesEndpoint: String
): ImagesSearch? = withContext(Dispatchers.IO) {
    try {
        Log.d("TAGINN","Fetching results")
        val results = searchImages(searchText)
        print(results.jsonResponse)
        return@withContext apiResponseToImages(results.jsonResponse)
    } catch (e: Exception) {
        Log.e("TAGINN", "catch getSearchWebResult ${e.message ?: ""}")
        return@withContext null
    }
}

fun searchImages(searchQuery: String): SearchResults {
    try {
        val safeSearchValue = URLEncoder.encode("Moderate", "UTF-8")
        val count = 18
        val offset = 1
//        val answerCount = 1
//        val responseFilter = URLEncoder.encode("webpages", "UTF-8")

        val urlString = "$imagesEndpoint?q=${URLEncoder.encode(searchQuery, "UTF-8")}" +
                "&safeSearch=${safeSearchValue}" +
                "&count=$count" +
                "&offset=$offset"
//                "&answerCount=$answerCount"+
//                "&responseFilter=$responseFilter"
        val url = URL(urlString)
        val connection = url.openConnection() as HttpsURLConnection
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey)
        val clientIP = InetAddress.getLocalHost().hostAddress
        connection.setRequestProperty("X-MSEdge-ClientIP", clientIP)
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
        Log.e("TAGINN", "catch searchWeb ${e.stackTraceToString() ?: ""}")
        return SearchResults(HashMap(), "")
    }
}

fun apiResponseToImages(jsonText: String): ImagesSearch {
    try {
        val parser = JsonParser()
        val json = parser.parse(jsonText) as JsonObject
        print(json)
        val gson = GsonBuilder().setPrettyPrinting().create()
        val prettyJson = gson.toJson(json)
        return gson.fromJson(prettyJson, ImagesSearch::class.java)
    } catch (e: Exception) {
        throw e
    }
}



//data class SearchResults(val relevantHeaders: HashMap<String, String>, val jsonResponse: String)
