package com.example.ipolitician.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.R
import com.example.ipolitician.structures.Question

class QuestionsRecyclerViewAdapter(private var questions: ArrayList<Question>) : RecyclerView.Adapter<QuestionsRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionsRecyclerViewHolder {
        return QuestionsRecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.question_holder, parent, false))
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    override fun onBindViewHolder(holder: QuestionsRecyclerViewHolder, position: Int) {
        holder.question.text = questions[position].question

//        holder.answers.adapter
    }
}
