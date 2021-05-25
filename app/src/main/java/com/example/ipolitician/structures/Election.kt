package com.example.ipolitician.structures

data class EV(
    val id: String = "",
    val title: String = "",
    val candidates: MutableMap<String,Int> = mutableMapOf()
)
