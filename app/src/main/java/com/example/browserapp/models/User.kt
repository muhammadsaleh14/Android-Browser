package com.example.browserapp.models

data class User(
    val email: String,
){
    val dictionary: Map<String, Any>
        get() = mapOf(
            "email" to email,
        )
}