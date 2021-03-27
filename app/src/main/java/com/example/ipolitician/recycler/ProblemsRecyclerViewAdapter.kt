package com.example.ipolitician.recycler

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.R
import com.example.ipolitician.structures.PV
import com.example.ipolitician.structures.Selected


class ProblemsRecyclerViewAdapter(private var problems: ArrayList<PV>, private var selected: Selected) : RecyclerView.Adapter<ProblemsRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProblemsRecyclerViewHolder {
        return ProblemsRecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.problem_holder, parent, false))
    }

    override fun getItemCount(): Int {
        return problems.size
    }

    override fun onBindViewHolder(holder: ProblemsRecyclerViewHolder, position: Int) {
        holder.problem.text = problems[position].problem

        holder.up_votes.text = problems[position].upvotes.toString()
        holder.down_votes.text = problems[position].downvotes.toString()
        holder.TotalVotes()

        if(selected.selected[position] == 1) {
            holder.uvoted = true
            holder.up_votes.setTextColor(Color.parseColor("#00ff04"))
        } else if(selected.selected[position] == -1) {
            holder.dvoted = true
            holder.down_votes.setTextColor(Color.parseColor("#ff0000"))
        }

        holder.up_votes.setOnClickListener {
            holder.UpVote()
        }

        holder.down_votes.setOnClickListener {
            holder.DownVote()
        }
    }

    fun getSelected(): Selected{
        return selected
    }
}
