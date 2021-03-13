package com.example.ipolitician.recycler

import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.R

class QuestionsRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val question: TextView
    val answers: RadioGroup

    init {
        question = itemView.findViewById<TextView>(R.id.question)
        answers = itemView.findViewById<RadioGroup>(R.id.answers)
    }

}