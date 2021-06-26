package com.example.ipolitician.structures

data class User(
    val password: String = "",
    val phoneNumber: String = "",
    val age: Int = 1,
    val gender: Int = 1,
    val region: String = "",
    val optional: List<String> = listOf()
)


