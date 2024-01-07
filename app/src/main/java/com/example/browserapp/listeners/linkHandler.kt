package com.example.browserapp.listeners

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.browserapp.activities.WebViewActivity

fun handleIntent(context: Context, url: String?) {
    try {
        if(url==null) return
        val uri = Uri.parse(url)
        val host = uri.host
        Log.d("TAGINN1","uri: $uri host: $host")

        // Check for known apps (e.g., YouTube)
//        val knownAppIntent = getLaunchIntentForPackage(context, host) // Pass context for packageManager
//        Log.d("TAGINN1","known app intent $knownAppIntent  context = $context" )
//        if (knownAppIntent != null) {
//            ContextCompat.startActivity(
//                context,
//                knownAppIntent,
//                null
//            ) // Pass context for startActivity
//            return
//        }
        val webIntent = Intent(Intent.ACTION_VIEW, uri)
        Log.d("TAGINN1","web intent $webIntent")
        if (context.packageManager.resolveActivity(webIntent, 0) != null) {
            ContextCompat.startActivity(context, webIntent, null) // Pass context for startActivity
            return
        }


        //if app is not found then go to webView
        val webViewIntent: Intent = Intent(context, WebViewActivity::class.java)
        webViewIntent.type
        webViewIntent.putExtra("url", url)
        context.startActivity(webViewIntent)

    }catch (e:Exception){
        Log.e("TAGINN",e.stackTraceToString())
    }


    // Check for generic website handling
//    val webIntent = Intent(Intent.ACTION_VIEW, uri)
//    Log.d("TAGINN1","web intent $webIntent")
//    if (context.packageManager.resolveActivity(webIntent, 0) != null) {
//        startActivity(context, webIntent, null) // Pass context for startActivity
//        return
//    }
//
//    // Fallback to WebView
//    val webView = WebView(context) // Create a WebView
//    webView.webViewClient = WebViewClient()
//    webView.loadUrl(url)

    // ... further WebView setup and display
}

// Helper function to check for known app intents
private fun getLaunchIntentForPackage(context: Context, packageName: String?): Intent? {
    return context.packageManager.getLaunchIntentForPackage(packageName?:"")
}
