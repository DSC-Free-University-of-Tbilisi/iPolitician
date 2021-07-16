package com.example.ipolitician.recycler

import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.R
import com.example.ipolitician.structures.PV


class ProblemsRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val problem: TextView
    val date: TextView
    val votes: Button
    val up_votes: Button
    val down_votes: Button

    var uvoted = false
    var dvoted = false
    var id: String = ""

    val upVoteClr = ContextCompat.getColor(itemView.context, R.color.upVoteClr)
    val downVoteClr = ContextCompat.getColor(itemView.context, R.color.downVoteClr)
    val noVoteClr = ContextCompat.getColor(itemView.context, R.color.noVoteClr)

    init {
        problem = itemView.findViewById(R.id.problem)
        date = itemView.findViewById(R.id.date)
        votes = itemView.findViewById(R.id.votes)
        up_votes = itemView.findViewById(R.id.up_vote)
        down_votes = itemView.findViewById(R.id.down_vote)
    }

    @Synchronized
    fun UpVote(): Int {
        if(dvoted) DownVote()
        uvoted = !uvoted

        var votes = up_votes.text.toString().toInt()
        if(up_votes.currentTextColor == noVoteClr) {
            votes++
            up_votes.text = votes.toString()
            up_votes.setTextColor(upVoteClr)
        } else {
            votes--
            up_votes.text = votes.toString()
            up_votes.setTextColor(noVoteClr)
        }
        TotalVotes()
        return votes
    }

    @Synchronized
    fun DownVote(): Int {
        if(uvoted) UpVote()
        dvoted = !dvoted

        var votes = down_votes.text.toString().toInt()
        if(down_votes.currentTextColor == noVoteClr) {
            votes++
            down_votes.text = votes.toString()
            down_votes.setTextColor(downVoteClr)
        } else {
            votes--
            down_votes.text = votes.toString()
            down_votes.setTextColor(noVoteClr)
        }
        TotalVotes()
        return votes
    }

    fun TotalVotes(): Int {
        var total_votes = up_votes.text.toString().toInt() + down_votes.text.toString().toInt()
        votes.text = "$total_votes"
        return total_votes
    }

    fun setID(id: String) {
        this.id = id
    }

    fun setVote(vote: Int = 0) {
        uvoted = vote == 1
        dvoted = vote == -1
        if(uvoted) {
            up_votes.setTextColor(upVoteClr)
            down_votes.setTextColor(noVoteClr)
        } else if(dvoted) {
            down_votes.setTextColor(downVoteClr)
            up_votes.setTextColor(noVoteClr)
        } else {
            down_votes.setTextColor(noVoteClr)
            up_votes.setTextColor(noVoteClr)
        }
    }

    fun getState() : PV {
        return PV(problem = problem.text.toString(), upvotes = up_votes.text.toString().toInt(), downvotes = down_votes.text.toString().toInt(), id=id)
    }
}