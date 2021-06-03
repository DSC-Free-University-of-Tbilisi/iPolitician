package com.example.ipolitician.structures

data class EV(
    val id: String = "",
    val title: String = "",
    val candidates: List<String> = listOf()
)

data class Vote(
    val votes: MutableMap<String, Int> = mutableMapOf()
)