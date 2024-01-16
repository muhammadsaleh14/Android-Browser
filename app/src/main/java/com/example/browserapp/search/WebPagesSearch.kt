package com.example.browserapp.search

import android.util.Log
import com.example.browserapp.dataClasses.bingSearch.WebpagesSearch
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

const val webSearchEndpoint: String = "https://api.bing.microsoft.com/v7.0/search"
fun main() {
    runBlocking {
        getSearchWebResultAsync(searchTerm, 1)
    }
}

suspend fun getSearchWebResultAsync(
    searchText: String,
    nextPageNumber: Int
): WebpagesSearch? = withContext(Dispatchers.IO) {
    try {
        Log.d("TAGINN","Fetching results")
        val results = searchWeb(searchText,nextPageNumber)
//        print(results.jsonResponse)
        Log.d("TAGINN2", results.jsonResponse)
        return@withContext parseAndPrettifyBingApiResponse(results.jsonResponse)
    } catch (e: Exception) {
        Log.e("TAGINN", "catch getSearchWebResult ${e.message ?: ""} \n $")
        return@withContext null
    }
}

fun searchWeb(searchQuery: String, nextPageNumber: Int): SearchResults {
    try {

        val safeSearchValue = URLEncoder.encode("Moderate", "UTF-8")
        val setLang = URLEncoder.encode("en", "UTF-8")
        //        val answerCount = 1
//        val responseFilter = URLEncoder.encode("webpages", "UTF-8")

        val urlString = "$webSearchEndpoint?q=${URLEncoder.encode(searchQuery, "UTF-8")}" +
                "&safeSearch=${safeSearchValue}" +
                "&count=$webpagesCount" +
                "&offset=$nextPageNumber"+
                "&setLang=$setLang"
//                "&answerCount=$answerCount"+
//                "&responseFilter=$responseFilter"
        Log.d("qqq","url=$urlString ")

        val url = URL(urlString)
        val connection = url.openConnection() as HttpsURLConnection
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey )
//        val clientIP = InetAddress.getLocalHost().hostAddress
//        connection.setRequestProperty("X-MSEdge-ClientIP", clientIP)
        val response = connection.inputStream.bufferedReader().use { it.readText() }
        Log.d("qqq","$response")

        val results = SearchResults(HashMap(), response)
        connection.headerFields.forEach { (header, values) ->
            Log.d("headers","$header : ${results.relevantHeaders[header]}" )
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

fun parseAndPrettifyBingApiResponse(jsonText: String): WebpagesSearch {
    try {
        val parser = JsonParser()
        val json = parser.parse(jsonText) as JsonObject
        print(json)
        val gson = GsonBuilder().setPrettyPrinting().create()
        val prettyJson = gson.toJson(json)
        return gson.fromJson(prettyJson, WebpagesSearch::class.java)
    } catch (e: Exception) {
        throw e
    }
}



