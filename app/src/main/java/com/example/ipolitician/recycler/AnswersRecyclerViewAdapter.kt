package com.example.ipolitician.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.R
import com.example.ipolitician.structures.Answer
import com.example.ipolitician.structures.Question


class AnswersRecyclerViewAdapter(private var answers: ArrayList<Answer>) : RecyclerView.Adapter<AnswersRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswersRecyclerViewHolder {
        return AnswersRecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.answer_holder, parent, false))
    }

    override fun getItemCount(): Int {
        return answers.size
    }

    override fun onBindViewHolder(holder: AnswersRecyclerViewHolder, position: Int) {
        holder.answer.text = answers[position].answer

    }
}
