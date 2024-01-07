package com.example.browserapp.viewmodels

import androidx.lifecycle.MutableLiveData

class ImageDetails {
    companion object {
        var isImageDetailFragmentOpen = false
        var name: String? = null
        var hostPageDisplayUrl: String? = null
        var thumbnailUrl: String? = null
        val closeDetailFragment = MutableLiveData(false)
        fun signalClose() {
            // Perform any validation or logic here
            closeDetailFragment.value = closeDetailFragment.value?.not()
        }
    }
}

