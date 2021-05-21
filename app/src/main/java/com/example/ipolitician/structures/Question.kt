package com.example.ipolitician.structures

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

data class QA(
    val question: String = "",
    val answers: List<String> = listOf()
)

data class Selected(
    val selected: ArrayList<Int> = arrayListOf(),
    var party: String = ""
)

data class Voted(
    val voted: MutableMap<String,Int> = mutableMapOf()
)

data class Party(
    val displayName: String = "",
    val selected: ArrayList<Int> = arrayListOf()
)