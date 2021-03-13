package com.example.ipolitician.recycler

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.R

class AnswersRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val answer: CheckBox

    init {
        answer = itemView.findViewById<CheckBox>(R.id.answer)
    }

}