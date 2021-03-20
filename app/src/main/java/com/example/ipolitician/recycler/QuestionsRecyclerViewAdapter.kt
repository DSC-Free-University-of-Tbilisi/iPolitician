package com.example.ipolitician.recycler

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.R
import com.example.ipolitician.firebase.QA
import com.example.ipolitician.structures.Question
import com.example.ipolitician.structures.Selected


class QuestionsRecyclerViewAdapter(private var questions: ArrayList<QA>, private var selected: Selected) : RecyclerView.Adapter<QuestionsRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionsRecyclerViewHolder {
        return QuestionsRecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.question_holder, parent, false))
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    override fun onBindViewHolder(holder: QuestionsRecyclerViewHolder, position: Int) {
        holder.question.text = (position+1).toString() + "). " + questions[position].question

        questions[position].answers.forEachIndexed { index, answer ->
            var ans = RadioButton(holder.answers.context)
            ans.text = answer
            holder.answers.addView(ans)
        }

        holder.answers.check(selected.selected[position])
        holder.answers.setOnCheckedChangeListener { _, i ->
            selected.selected[position] = i
        }
    }

    fun getSelected(): Selected{
        return selected
    }
}
