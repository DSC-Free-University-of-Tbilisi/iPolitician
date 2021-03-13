package com.example.ipolitician.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.R
import com.example.ipolitician.structures.Answer
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

        var A1 = Answer(id=0, answer= "A1")
        var A2 = Answer(id=0, answer= "A2")
        var A3 = Answer(id=0, answer= "A3")
        var answers = ArrayList<Answer>()
        answers.add(0,A1)
        answers.add(1,A2)
        answers.add(2,A3)

        for(answer in answers) {
            var ans = RadioButton(holder.answers.context)
            ans.text = answer.answer
            holder.answers.addView(ans)
        }


    }
}
