package com.example.browserapp.listeners

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.View

fun changeColor(view: View) {
//    val drawable = view.background
//    val colorFilter = drawable.colorFilter
//    var color = if (colorFilter != null) colorFilter.color else Color.WHITE
//    val originalColor = view.backgroundTintList?.defaultColor ?: Color.WHITE
//
    var color = Color.TRANSPARENT
    val background = view.background
    if (background is ColorDrawable) color = background.color
    view.setBackgroundColor(Color.CYAN)
    Handler(Looper.getMainLooper()).postDelayed({
        view.setBackgroundColor(color)
    }, 500)
}

