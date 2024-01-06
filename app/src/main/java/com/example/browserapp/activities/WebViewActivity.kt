package com.example.browserapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import com.example.browserapp.R

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        try{
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_webpage)
            var urlEditEditText = findViewById<EditText>(R.id.searchUrl)
            var webView = findViewById<WebView>(R.id.webView)


            // Set a WebViewClient to handle the URL loading within the WebView
            webView.webViewClient = WebViewClient()

            // Load a specific URL
            val receivedUrl = intent.getStringExtra("url")
            urlEditEditText.setText(receivedUrl)
            webView.loadUrl(receivedUrl?:"")
        }catch (e:Exception){
            Log.e("TAGINN1",e.stackTraceToString())
        }



    }
}