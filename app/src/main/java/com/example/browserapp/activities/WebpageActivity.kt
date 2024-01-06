package com.example.browserapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import com.example.browserapp.R

class WebpageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webpage)
        var urleditEditText = findViewById<EditText>(R.id.searchUrl)
        var webView = findViewById<WebView>(R.id.webView)


        // Set a WebViewClient to handle the URL loading within the WebView
        webView.webViewClient = WebViewClient()

        // Load a specific URL
        val url = "https://www.geeksforgeeks.org/array-data-structure/"
        urleditEditText.setText(url)
        webView.loadUrl(url)


    }
}