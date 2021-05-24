package com.example.ipolitician.recycler

import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.R

class VocabularyRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val header: TextView = itemView.findViewById<TextView>(R.id.vocab_header)
    val description: TextView = itemView.findViewById<TextView>(R.id.vocab_description)
}