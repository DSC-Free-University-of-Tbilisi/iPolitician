package com.example.ipolitician.recycler

import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.R
import com.example.ipolitician.structures.QA
import com.example.ipolitician.structures.Selected
import com.example.ipolitician.textColor


class QuestionsRecyclerViewAdapter(
    private var questions: ArrayList<QA>,
    private var selected: Selected
) : RecyclerView.Adapter<QuestionsRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionsRecyclerViewHolder {
        return QuestionsRecyclerViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.holder_question,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    private fun clearHolder(holder: QuestionsRecyclerViewHolder) {
        val count: Int = holder.answers.childCount
        if (count > 0) {
            for (i in count - 1 downTo 0) {
                val o = holder.answers.getChildAt(i)
                if (o is RadioButton) {
                    holder.answers.removeViewAt(i)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: QuestionsRecyclerViewHolder, position: Int) {
        clearHolder(holder)
        holder.question.text = (position+1).toString() + "). " + questions[position].question
        for (answer in questions[position].answers){
            var ans = RadioButton(holder.answers.context)
            ans.text = answer
            ans.setTextColor(textColor.data)
            ans.buttonTintList = ColorStateList.valueOf(textColor.data)
            holder.answers.addView(ans)
        }
        Log.d("aeeee", selected.selected[position].toString() + " " + position.toString())
        if(selected.selected[position] != -1) {
            (holder.answers.getChildAt(selected.selected[position]) as RadioButton).isChecked = true
        }
        holder.answers.setOnCheckedChangeListener { radio, _ ->
            selected.selected[holder.adapterPosition] = radio.indexOfChild(radio.findViewById(radio.checkedRadioButtonId))
        }
    }

    fun getSelected(): Selected {
        return selected
    }
}
