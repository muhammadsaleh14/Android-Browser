package com.example.browserapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel:ViewModel() {
    val isLoading = MutableLiveData(false)
    val isOnline = MutableLiveData(false)
}