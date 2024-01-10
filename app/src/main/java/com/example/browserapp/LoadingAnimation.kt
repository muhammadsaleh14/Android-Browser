package com.example.browserapp

import android.view.View
import com.airbnb.lottie.LottieAnimationView
import com.example.browserapp.fragments.LoadingDialogFragment

class LoadingAnimation(private val loadingAnimation: LottieAnimationView) {

    fun startLoading() {
        loadingAnimation.visibility = View.VISIBLE
        loadingAnimation.playAnimation()
    }
//
//    // Stop the animation when loading finishes
    fun stopLoading() {
        loadingAnimation.visibility = View.GONE
        loadingAnimation.cancelAnimation()
    }
}