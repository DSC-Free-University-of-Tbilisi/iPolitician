package com.example.ipolitician.recycler


import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.MainActivity
import com.example.ipolitician.R
import com.example.ipolitician.firebase.DataAPI
import com.example.ipolitician.structures.EV
import com.example.ipolitician.structures.Vote
import com.example.ipolitician.structures.Voted
import com.example.ipolitician.textColor


class ElectionRecyclerViewAdapter(private var elections: ArrayList<EV>, private var votes: Vote, private var voted: Voted) : RecyclerView.Adapter<ElectionRecyclerViewHolder>() {

    private var DB = DataAPI.instance

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionRecyclerViewHolder {
        return ElectionRecyclerViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.holder_election,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return elections.size
    }

    private fun clearHolder(holder: ElectionRecyclerViewHolder) {
        val count: Int = holder.candidates.childCount
        if (count > 0) {
            for (i in count - 1 downTo 0) {
                val o = holder.candidates.getChildAt(i)
                if (o is RadioButton) {
                    holder.candidates.removeViewAt(i)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onBindViewHolder(holder: ElectionRecyclerViewHolder, position: Int) {
        clearHolder(holder)

        holder.election_title.text = elections[position].title


        for (candidate in elections[position].candidates){
            var cand = RadioButton(holder.candidates.context)
            val key = candidate + "+" + elections[position].title

            cand.text = candidate + ": ${votes.votes[key]}"

            cand.setTextColor(textColor.data)
            cand.buttonTintList = ColorStateList.valueOf(textColor.data)
            holder.candidates.addView(cand)
        }

        if(voted.voted.containsKey(elections[position].id)) {
            (holder.candidates.getChildAt(voted.voted[elections[position].id]!!) as RadioButton).isChecked = true
        }

        holder.vote.setOnClickListener { it ->
            val radio = holder.candidates
            val pos = holder.adapterPosition

            if(elections[pos].region.isNotEmpty() && elections[pos].region != MainActivity.user!!.region) {
                Toast.makeText(
                    it.context,
                    "??????????????? ?????? ??????????????????????????? ?????? ???????????????????????? ???????????? ??????????????????!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if(voted.voted.containsKey(elections[pos].id)) {
                var oldId = (holder.candidates.getChildAt(voted.voted[elections[pos].id]!!) as RadioButton).text.split(":")[0]
                oldId += "+" + elections[pos].title
                DB.unvoteElection(oldId)
            }

            voted.voted[elections[pos].id] = radio.indexOfChild(
                radio.findViewById(
                    radio.checkedRadioButtonId
                )
            )
            DB.setUserElections(MainActivity.uniqueID!!, voted)

            var newId = (radio.getChildAt(voted.voted[elections[pos].id]!!) as RadioButton).text.split(":")[0]
            newId += "+" + elections[pos].title
            DB.voteElection(newId)

            Toast.makeText(
                it.context,
                "??????????????? ????????? ?????????????????? '" + newId.split("+")[0] +"'-???",
                Toast.LENGTH_SHORT
            ).show()

            fetch_data()
        }

    }

    fun fetch_data() {
        DB.getElections { elections ->
            DB.getElectionVotes { votes ->
                this.elections = elections
                this.votes = votes
                notifyDataSetChanged()
            }
        }
    }
}
