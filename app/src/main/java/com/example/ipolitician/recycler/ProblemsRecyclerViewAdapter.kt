package com.example.ipolitician.recycler

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.MainActivity
import com.example.ipolitician.R
import com.example.ipolitician.firebase.DataAPI
import com.example.ipolitician.structures.PV
import com.example.ipolitician.structures.Voted
import kotlinx.android.synthetic.main.holder_problem.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ProblemsRecyclerViewAdapter(private var problems: ArrayList<PV>, private var voted: Voted) : RecyclerView.Adapter<ProblemsRecyclerViewHolder>(), FilterableRecyclerView  {

    private var DB = DataAPI.instance

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProblemsRecyclerViewHolder {
        return ProblemsRecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.holder_problem, parent, false))
    }

    override fun getItemCount(): Int {
        return problems.size
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ProblemsRecyclerViewHolder, position: Int) {
        holder.problem.text = problems[position].problem

        holder.up_votes.text = problems[position].upvotes.toString()
        holder.down_votes.text = problems[position].downvotes.toString()
        holder.TotalVotes()

        holder.setID(problems[position].id)
        holder.setVote(voted.voted.getOrDefault(problems[position].id, 0))

        val sfd = SimpleDateFormat("dd.MM.yyyy")
        holder.date.text = sfd.format(problems[position].date.toDate())

        holder.up_votes.setOnClickListener {
            val reg = problems[holder.adapterPosition].region
            if(reg.isNotEmpty() && MainActivity.user!!.region != reg) {
                Toast.makeText(it.context, "თქვენ არ შეგიძლიათ ამ რეგიონში ხმის მიცემა!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            holder.UpVote()
            update(1, holder.getState())
        }

        holder.down_votes.setOnClickListener {
            val reg = problems[holder.adapterPosition].region
            if(reg.isNotEmpty() && MainActivity.user!!.region != reg) {
                Toast.makeText(it.context, "თქვენ არ შეგიძლიათ ამ რეგიონში ხმის მიცემა!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
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
