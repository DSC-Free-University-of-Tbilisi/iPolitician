package com.example.ipolitician.structures

data class QA(
    val question: String = "",
    val answers: List<String> = listOf()
)

data class Selected(
    val selected: ArrayList<Int> = arrayListOf()
)