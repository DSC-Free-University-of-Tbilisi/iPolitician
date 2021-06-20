package com.example.ipolitician.structures

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EV(
    val id: String = "",
    val title: String = "",
    val candidates: List<String> = listOf(),
    val region: String = ""
) : Parcelable


data class Vote(
    val votes: MutableMap<String, Int> = mutableMapOf()
)