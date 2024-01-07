package com.example.browserapp.listeners

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.load.engine.executor.GlideExecutor.UncaughtThrowableStrategy.LOG
import com.example.browserapp.activities.WebViewActivity

fun onVideoCardClick(view: View, context: Context,url: String?) {
    view.setOnClickListener {
        changeColor(view)
        handleIntent(context, url) // Replace with actual URL
    }
}

