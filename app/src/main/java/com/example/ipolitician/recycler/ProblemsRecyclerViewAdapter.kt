package com.example.ipolitician.recycler

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.MainActivity
import com.example.ipolitician.R
import com.example.ipolitician.firebase.DataAPI
import com.example.ipolitician.structures.PV
import com.example.ipolitician.structures.Voted


class ProblemsRecyclerViewAdapter(private var problems: ArrayList<PV>, private var voted: Voted) : RecyclerView.Adapter<ProblemsRecyclerViewHolder>(), FilterableRecyclerView  {

    private var DB = DataAPI.instance

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProblemsRecyclerViewHolder {
        return ProblemsRecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.holder_problem, parent, false))
    }

    override fun getItemCount(): Int {
        return problems.size
    }

    override fun onBindViewHolder(holder: ProblemsRecyclerViewHolder, position: Int) {
        holder.problem.text = problems[position].problem

        holder.up_votes.text = problems[position].upvotes.toString()
        holder.down_votes.text = problems[position].downvotes.toString()
        holder.TotalVotes()

        val key = problems[position].id
        holder.setID(key)

        if(voted.voted.containsKey(key)) {
            if(voted.voted[key] == 1) {
                holder.uvoted = true
                holder.up_votes.setTextColor(Color.parseColor("#00ff04"))
                holder.down_votes.setTextColor(Color.parseColor("#FFFFFF"))
            } else if(voted.voted[key] == -1) {
                holder.dvoted = true
                holder.down_votes.setTextColor(Color.parseColor("#ff0000"))
                holder.up_votes.setTextColor(Color.parseColor("#FFFFFF"))
            }
        }


        holder.up_votes.setOnClickListener {
            holder.UpVote()
            update(1, holder.getState())
        }

        holder.down_votes.setOnClickListener {
            holder.DownVote()
            update(-1, holder.getState())
        }
    }

    fun update(v: Int, p: PV) {
        if(voted.voted.containsKey(p.id)) {
            if(voted.voted[p.id] == v) {
                voted.voted.remove(p.id)

                if(v == 1) DB.voteProblem(p.id, -1, 0)
                else DB.voteProblem(p.id, 0, -1)
            } else {
                voted.voted[p.id] = v

                if(v == 1) DB.voteProblem(p.id, 1, -1)
                else DB.voteProblem(p.id, -1, 1)
            }
        } else {
            voted.voted[p.id] = v

            if(v == 1) DB.voteProblem(p.id, 1, 0)
            else DB.voteProblem(p.id, 0, 1)
        }

        DB.setUserProblems(MainActivity.uniqueID!!, voted)
    }

    fun search(query: String) {
        DB.getProblems() { problems ->
            DB.getUserProblems(MainActivity.uniqueID!!) { voted ->
                this.problems = problems.filter { it.problem.contains(query) } as ArrayList<PV>
                this.voted = voted
                notifyDataSetChanged()
            }
        }
    }

    fun sortBy(type: Int, vector: Int) {
        DB.getProblems() { problems ->
            DB.getUserProblems(MainActivity.uniqueID!!) { voted ->
                when(type) {
                    1 -> {
                        problems.sortBy { vector*it.upvotes }
                    }
                    -1 -> {
                        problems.sortBy { vector*it.downvotes }
                    }
                    else -> {
                        problems.sortBy { vector*(it.upvotes + it.downvotes)}
                    }
                }
                this.problems = problems
                this.voted = voted
                notifyDataSetChanged()
            }
        }
    }

    fun fetch_data() {
        DB.getProblems() { problems ->
            DB.getUserProblems(MainActivity.uniqueID!!) { voted ->
                this.problems = problems
                this.voted = voted
                notifyDataSetChanged()
            }
        }
    }

    fun getVoted(): Voted {
        return voted
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                return FilterResults()
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                search(constraint.toString())
            }
        }
    }
}
