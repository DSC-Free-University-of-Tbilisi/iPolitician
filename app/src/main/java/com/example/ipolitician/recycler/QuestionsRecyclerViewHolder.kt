package com.example.ipolitician.recycler

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.R

class QuestionsRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val question: TextView
    val answers: RecyclerView

    init {
        question = itemView.findViewById<TextView>(R.id.question)
        answers = itemView.findViewById<RecyclerView>(R.id.answers_recyclerview)
    }

}