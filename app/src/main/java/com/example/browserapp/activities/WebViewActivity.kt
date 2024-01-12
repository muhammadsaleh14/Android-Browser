package com.example.browserapp.activities

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewStub
import android.view.animation.AnimationUtils
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.browserapp.R
import java.util.Stack

class WebViewActivity : AppCompatActivity() {
    private val urlStack = Stack<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webpage)


        class MyWebViewClient : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                // Update the EditText with the new URL
                val editText =
                    findViewById<EditText>(R.id.searchUrl) // Replace with your EditText's ID
                editText.setText(url)
                if (!urlStack.contains(url)) {
                    urlStack.push(url)
                }
            }
        }

        var urlEditEditText = findViewById<EditText>(R.id.searchUrl)
        val webView = findViewById<WebView>(R.id.webView) // Replace with your WebView's ID
        webView.webViewClient = MyWebViewClient()

        try {
            // Load a specific URL
            val receivedUrl = intent.getStringExtra("url")
            urlEditEditText.setText(receivedUrl)
            webView.loadUrl(receivedUrl ?: "")
        } catch (e: Exception) {
            Log.e("TAGINN1", e.stackTraceToString())
        }
        val showOptionsButton = findViewById<ImageButton>(R.id.showOptionsBtn)
        val optionsListStub = findViewById<ViewStub>(R.id.options_list_stub)
        val optionsList: View? = optionsListStub.inflate()
        if (optionsList != null) {
            optionsList.visibility = View.GONE
        }
        showOptionsButton.setOnClickListener {
            if (optionsList?.visibility == View.GONE) {
                // Slide in the options list
                optionsList.visibility = View.VISIBLE
                val slideInAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left)
                optionsList.startAnimation(slideInAnimation)
                showOptionsButton.setImageResource(R.drawable.arrow_right)
            } else {
                // Slide out the options list
                val slideOutAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out_to_left)
                optionsList?.startAnimation(slideOutAnimation)
                optionsList?.visibility = View.GONE
                showOptionsButton.setImageResource(R.drawable.arrow_left)
            }
        }

    }

    override fun onBackPressed() {
        val webView = findViewById<WebView>(R.id.webView) // Replace with your WebView's ID
//        if (webView.canGoBack()) {
//
//            // If WebView can go back, use its history
//            webView.goBack()
//        } else
        if (urlStack.size > 1) {
            // If WebView can't go back, but stack has URLs, pop and load
            urlStack.pop()
            val previousUrl = urlStack.pop()
            webView.loadUrl(previousUrl)
        } else {
            // If both WebView history and stack are empty, go to previous activity
            super.onBackPressed()
        }
    }
}