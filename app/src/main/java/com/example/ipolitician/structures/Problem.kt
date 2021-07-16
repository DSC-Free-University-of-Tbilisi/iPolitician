package com.example.ipolitician.structures

import com.google.firebase.Timestamp

data class PV(
    val region: String = "",
    val problem: String = "",
    val upvotes: Int = 0,
    val downvotes: Int = 0,
    val id: String = "",
    val date: Timestamp = Timestamp.now()
    )

