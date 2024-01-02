package com.example.browserapp.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.browserapp.R
import com.example.browserapp.adapters.SearchAdapter
import com.example.browserapp.dataClasses.bingSearch.BingSearch
import com.example.browserapp.endpoint
import com.example.browserapp.getSearchWebResult
import com.example.browserapp.subscriptionKey
import com.google.gson.Gson
import com.google.gson.JsonParser
import java.io.InputStreamReader

class SearchActivity : AppCompatActivity() {

    private lateinit var rvSearchResult: RecyclerView
    private lateinit var BingSearch: BingSearch
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        rvSearchResult = findViewById(R.id.rvSearchResult)
        rvSearchResult.layoutManager = LinearLayoutManager(this)
        rvSearchResult.setHasFixedSize(true)
        Log.d("TAGINN","SUB KEY = ${subscriptionKey}")
        val webSearchValues = getSearchWebResult("gol gappay", subscriptionKey, endpoint)?.webPages?.value

        Log.d("TAGINN", webSearchValues.toString())
        rvSearchResult.adapter = SearchAdapter(webSearchValues)
    }

    fun readJsonToBingSearch(): BingSearch {
        try {
            val parser = JsonParser()
//            val projectDir = File(System.getProperty("user.dir"))
//            val jsonFilePath = projectDir.resolve("app/src/main/java/com/example/browserapp/response.json").absolutePath
            val inputStream = assets.open("../response.json") // Adjust for different locations
            val reader = InputStreamReader(inputStream)
            val jsonData = reader.readText()
            val gsons = Gson()
            val parsedData: BingSearch = gsons.fromJson(jsonData, BingSearch::class.java)
//            val jsonFile = File()
//            val jsonText = jsonFile.readText()
//            val json = parser.parse(jsonText) as JsonObject
            return parsedData

//            val gson = GsonBuilder().setPrettyPrinting().create()
//            val prettyJson = gson.toJson(json)  // Prettify for readability
//
//            return gson.fromJson(prettyJson, com.example.browserapp.dataClasses.bingSearch.BingSearch::class.java)
        }catch (e:Exception){
//            Log.e("TAGINN", e.message?:"")
        }
        return BingSearch
    }
}