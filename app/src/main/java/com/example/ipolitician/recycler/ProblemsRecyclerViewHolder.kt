package com.example.ipolitician.recycler

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.R

class ProblemsRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val problem: TextView
    val votes: Button
    val up_votes: Button
    val down_votes: Button

    var uvoted = false
    var dvoted = false

    init {
        problem = itemView.findViewById(R.id.problem)
        votes = itemView.findViewById(R.id.votes)
        up_votes = itemView.findViewById(R.id.up_vote)
        down_votes = itemView.findViewById(R.id.down_vote)
    }

    fun UpVote(): Int {
        if(dvoted) DownVote()
        uvoted = !uvoted

        var votes = up_votes.text.toString().toInt()
        if(up_votes.currentTextColor == Color.parseColor("#FFFFFF")) {
            votes++
            up_votes.text = votes.toString()
            up_votes.setTextColor(Color.parseColor("#00ff04"))
        } else {
            votes--
            up_votes.text = votes.toString()
            up_votes.setTextColor(Color.parseColor("#FFFFFF"))
        }
        TotalVotes()
        return votes
    }

    fun DownVote(): Int {
        if(uvoted) UpVote()
        dvoted = !dvoted

        var votes = down_votes.text.toString().toInt()
        if(down_votes.currentTextColor == Color.parseColor("#FFFFFF")) {
            votes++
            down_votes.text = votes.toString()
            down_votes.setTextColor(Color.parseColor("#ff0000"))
        } else {
            votes--
            down_votes.text = votes.toString()
            down_votes.setTextColor(Color.parseColor("#FFFFFF"))
        }
        TotalVotes()
        return votes
    }

    fun TotalVotes(): Int {
        var total_votes = up_votes.text.toString().toInt() + down_votes.text.toString().toInt()
        votes.text = "Votes: $total_votes"
        return total_votes
    }

}