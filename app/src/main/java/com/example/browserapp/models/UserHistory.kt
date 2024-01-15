package com.example.browserapp.models

data class UserHistory (
    val url : String,
    val name : String,
    val timestamp : Long = 0

){

    val dictionary: Map<String, Any>
        get() = mapOf(
            "url" to url,
            "name" to name,
            "timeStamp" to timestamp
        )

}