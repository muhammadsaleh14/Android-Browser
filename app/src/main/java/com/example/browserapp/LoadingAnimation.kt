package com.example.browserapp

import android.view.View
import com.airbnb.lottie.LottieAnimationView
import com.example.browserapp.fragments.LoadingDialogFragment

class LoadingAnimation() {


    private val loadingDialogFragment = LoadingDialogFragment()

// Show the dialog and start animation


// ... perform loading tasks

// Stop animation and dismiss dialog
    loadingDialogFragment.stopAnimation()
    loadingDialogFragment.dismiss()

    fun startLoading(a:supportFragmentManager) {
    loadingDialogFragment.show(a, "loading_dialog")
    loadingDialogFragment.startAnimation()
//        loadingAnimation.visibility = View.VISIBLE
//        loadingAnimation.playAnimation()
    }
//
//    // Stop the animation when loading finishes
//    fun stopLoading() {
//        loadingAnimation.visibility = View.GONE
//        loadingAnimation.cancelAnimation()
//    }
}