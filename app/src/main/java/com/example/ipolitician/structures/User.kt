package com.example.ipolitician.structures

import com.google.firebase.Timestamp

data class User(
    val password: String = "",
    val phoneNumber: String = "",
    val age: Int = 1,
    val gender: Int = 0,
    val region: String = "",
    val optional: List<String> = listOf()
)

data class TM(
    val timestamp: Timestamp = Timestamp.now()
)

