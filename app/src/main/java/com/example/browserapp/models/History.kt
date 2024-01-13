package com.example.browserapp.models

data class History (
    val url : String,
    val name : String,
    val timestamp : Long = 0

){

    val dictionary: Map<String, Any>
        get() = mapOf(
            "url" to String,
            "name" to name,
            "timeStamp" to Long
        )

}