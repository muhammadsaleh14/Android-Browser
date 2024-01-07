package com.example.browserapp.listeners

import android.content.Context
import android.view.View

fun webpagesListener(view: View, context: Context,url: String?) {
    view.setOnClickListener {
        changeColor(view)
        handleIntent(view.context, url) // Replace with actual URL
    }
}