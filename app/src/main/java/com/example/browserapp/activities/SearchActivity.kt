package com.example.browserapp.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.browserapp.R
import com.example.browserapp.adapters.SearchAdapter
import com.example.browserapp.dataClasses.bingSearch.BingSearch
import com.example.browserapp.endpoint
import com.example.browserapp.getSearchWebResultAsync
import com.example.browserapp.searchTerm
import com.example.browserapp.subscriptionKey
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

class SearchActivity : AppCompatActivity() {

    private lateinit var rvSearchResult: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        rvSearchResult = findViewById(R.id.rvSearchResult)
        rvSearchResult.layoutManager = LinearLayoutManager(this)
        rvSearchResult.setHasFixedSize(true)


        lifecycleScope.launch { 
            val webSearchValues: BingSearch? = getSearchWebResultAsync(searchTerm, subscriptionKey, endpoint)
//            val webSearchValues:BingSearch? = readJsonToBingSearch()
            withContext(Dispatchers.Main) {
                if (webSearchValues != null) {
                    rvSearchResult.adapter = SearchAdapter(webSearchValues.webPages?.value)
                } else {

                    // Handle error cases
                }
            }
        }
    }

    fun readJsonToBingSearch(): BingSearch? {
        try {

            val inputStream = assets.open("response.json")
            val reader = InputStreamReader(inputStream)
            val jsonData = reader.readText()
            val gson = Gson()
            return gson.fromJson(jsonData, BingSearch::class.java)
        } catch (e: Exception) {
            Log.d("TAGINN", "reading json error ${e.stackTraceToString()}")
        }
        return null
    }
}
