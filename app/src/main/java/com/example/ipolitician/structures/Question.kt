package com.example.ipolitician.structures

data class Question(
    val id: Int,
    val question: String
)

data class Selected(
    val selected: ArrayList<Int> = arrayListOf()
)