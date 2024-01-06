package com.example.browserapp.listeners

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat.startActivity

fun onVideoCardClick(view: View, context: Context,url: String?) {
    view.setOnClickListener {
        changeColor(view)
        handleIntent(view.context, url) // Replace with actual URL
    }
}

fun changeColor(view: View) {
    view.setBackgroundColor(Color.BLUE)
    Handler(Looper.getMainLooper()).postDelayed({
        view.setBackgroundColor(Color.WHITE)
    }, 500)
}

fun handleIntent(context: Context, url: String?) {
    if(url==null) return
    val uri = Uri.parse(url)
    val host = uri.host
    Log.d("TAGINN1","uri: $uri host: $host")

    // Check for known apps (e.g., YouTube)
    val knownAppIntent = getLaunchIntentForPackage(context, host) // Pass context for packageManager
    Log.d("TAGINN1","known app intent $knownAppIntent")
    if (knownAppIntent != null) {
        startActivity(context, knownAppIntent, null) // Pass context for startActivity
        return
    }


    // Check for generic website handling
    val webIntent = Intent(Intent.ACTION_VIEW, uri)
    Log.d("TAGINN1","web intent $webIntent")
    if (context.packageManager.resolveActivity(webIntent, 0) != null) {
        startActivity(context, webIntent, null) // Pass context for startActivity
        return
    }

    // Fallback to WebView
    val webView = WebView(context) // Create a WebView
    webView.webViewClient = WebViewClient()
    webView.loadUrl(url)

    // ... further WebView setup and display
}

// Helper function to check for known app intents
private fun getLaunchIntentForPackage(context: Context, packageName: String?): Intent? {
    return context.packageManager.getLaunchIntentForPackage(packageName?:"")
}
