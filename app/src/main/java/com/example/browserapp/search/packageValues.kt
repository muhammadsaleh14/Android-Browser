package com.example.browserapp.search

import com.example.browserapp.BuildConfig

var subscriptionKey:String = BuildConfig.SUBSCRIPTION_KEY
const val searchTerm = "osmama bin ladin"
data class SearchResults(val relevantHeaders: HashMap<String, String>, val jsonResponse: String)

val webpagesCount = 20
val videosCount =  20
val imagesCount = 20

//IDK what this ftn does below
//fun prettify(jsonText: String): String {
//    val parser = JsonParser()
//    val json = parser.parse(jsonText) as JsonObject
//    val gson = GsonBuilder().setPrettyPrinting().create()
//    return gson.toJson(json)
//}