package com.example.ipolitician.recycler

import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.R

class ElectionRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val election_title: TextView
    val candidates: RadioGroup
    val vote: Button

    init {
        election_title = itemView.findViewById<TextView>(R.id.election_title)
        candidates = itemView.findViewById<RadioGroup>(R.id.candidates)
        vote = itemView.findViewById<Button>(R.id.confirm_vote)
    }

}