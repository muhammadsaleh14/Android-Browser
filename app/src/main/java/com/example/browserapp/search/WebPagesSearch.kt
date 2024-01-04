package com.example.browserapp.search

import android.util.Log
import com.example.browserapp.dataClasses.bingSearch.BingSearch
import com.example.browserapp.BuildConfig
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection

var subscriptionKey:String = BuildConfig.SUBSCRIPTION_KEY
const val webSearchEndpoint: String = "https://api.bing.microsoft.com/v7.0/search"
const val searchTerm = "gol gappay"
fun main() {
    runBlocking {
        getSearchWebResultAsync("multan videos", subscriptionKey, webSearchEndpoint)
    }
}

suspend fun getSearchWebResultAsync(
    searchText: String,
    subscriptionKey : String?,
    endpoint: String
): BingSearch? = withContext(Dispatchers.IO) {
    try {

        Log.d("TAGINN","Fetching results")
        val results = searchWeb(searchText)
//        print(results.jsonResponse)
        return@withContext parseAndPrettifyBingApiResponse(results.jsonResponse)
    } catch (e: Exception) {
        Log.e("TAGINN", "catch getSearchWebResult ${e.message ?: ""}")
        return@withContext null
    }
}

fun searchWeb(searchQuery: String): SearchResults {
    try {

        val safeSearchValue = URLEncoder.encode("Moderate", "UTF-8")
        val count = 10
        val offset = 1
//        val answerCount = 1
//        val responseFilter = URLEncoder.encode("webpages", "UTF-8")

        val urlString = "$webSearchEndpoint?q=${URLEncoder.encode(searchQuery, "UTF-8")}" +
                "&safeSearch=${safeSearchValue}" +
                "&count=$count" +
                "&offset=$offset"
//                "&answerCount=$answerCount"+
//                "&responseFilter=$responseFilter"
        val url = URL(urlString)
        val connection = url.openConnection() as HttpsURLConnection
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey )
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

fun parseAndPrettifyBingApiResponse(jsonText: String): BingSearch {
    try {
        val parser = JsonParser()
        val json = parser.parse(jsonText) as JsonObject
        print(json)
        val gson = GsonBuilder().setPrettyPrinting().create()
        val prettyJson = gson.toJson(json)
        return gson.fromJson(prettyJson, BingSearch::class.java)
    } catch (e: Exception) {
        throw e
    }
}

fun prettify(jsonText: String): String {
    val parser = JsonParser()
    val json = parser.parse(jsonText) as JsonObject
    val gson = GsonBuilder().setPrettyPrinting().create()
    return gson.toJson(json)
}

data class SearchResults(val relevantHeaders: HashMap<String, String>, val jsonResponse: String)
