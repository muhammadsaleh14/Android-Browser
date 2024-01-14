package com.example.browserapp.listeners

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.load.engine.executor.GlideExecutor.UncaughtThrowableStrategy.LOG
import com.example.browserapp.activities.WebViewActivity

fun onImageCardClick(view: View, context: Context,url: String?, name:String?) {
    view.setOnClickListener {
        changeColor(view)
        handleIntent(view.context, url , name) // Replace with actual URL
    }
}


