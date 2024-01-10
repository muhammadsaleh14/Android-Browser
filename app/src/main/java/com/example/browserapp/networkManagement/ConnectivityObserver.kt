package com.example.browserapp.networkManagement

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observe(): Flow<Status>
    abstract fun getCurrentStatus(): ConnectivityObserver.Status
    enum class Status {
        Available, Unavailable, Losing, Lost
    }
}