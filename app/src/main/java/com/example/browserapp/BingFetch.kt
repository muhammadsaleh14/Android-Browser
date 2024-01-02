package com.example.browserapp

import android.util.Log
import com.example.browserapp.dataClasses.bingSearch.BingSearch
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection

// Access environment variables directly in Kotlin
//val subscriptionKey: String? = System.getenv("BING_SEARCH_V7_SUBSCRIPTION_KEY")
//val endpoint: String = (System.getenv("BING_SEARCH_V7_ENDPOINT") ?: "") + "/v7.0/search"
const val subscriptionKey: String = "a9ec21e2545c4c368d7275059df7f799"
const val endpoint: String = "https://api.bing.microsoft.com/v7.0/search"
const val searchTerm = "temperature in my city"
//val allenvs = System.getenv()
//fun main(){
//    val bingSearch = getSearchWebResult("temp")
//    for (item in bingSearch?.webPages?.value ?: emptyList()) {
//        println(item)
//    }
//}
suspend fun getSearchWebResultAsync(
    searchText: String,
    subscriptionKey: String?,
    endpoint: String
): BingSearch? = withContext(Dispatchers.IO) {
    try {
        val results = searchWeb(searchText)
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
//    val responseFilter = URLEncoder.encode("Webpages", "UTF-8")
        val urlString = "$endpoint?q=${URLEncoder.encode(searchQuery, "UTF-8")}" +
                "&safeSearch=${safeSearchValue}" +
                "&count=$count" +
                "&offset=$offset"
//            "&responseFilter=$responseFilter"
        val url = URL(urlString)
        val connection = url.openConnection() as HttpsURLConnection
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey)
        Log.d("TAGINN", "getting results ${"aaaaa" ?: ""}")
        val clientIP = InetAddress.getLocalHost().hostAddress
        connection.setRequestProperty("X-MSEdge-ClientIP", clientIP)
        val response = connection.inputStream.bufferedReader().use { it.readText() }
        Log.d("TAGINN", "getting results ${"kkkkk" ?: ""}")
        val results = SearchResults(HashMap(), response)

        connection.headerFields.forEach { (header, values) ->
            if (header != null && (header.startsWith("BingAPIs-") || header.startsWith("X-MSEdge-"))) {
                results.relevantHeaders[header] = values[0]
            }
        }

        return results
    } catch (e: Exception) {
        Log.e("TAGINN", "catch searchWeb ${e.stackTraceToString() ?: ""}")
        return SearchResults(HashMap(), "")
    }
}

fun parseAndPrettifyBingApiResponse(jsonText: String): BingSearch {
    try {
        val parser = JsonParser()
        val json = parser.parse(jsonText) as JsonObject
        val gson = GsonBuilder().setPrettyPrinting().create()
        val prettyJson = gson.toJson(json)  // Prettify for readability

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


