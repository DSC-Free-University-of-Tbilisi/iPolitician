package com.example.ipolitician.recycler


import android.app.ActionBar
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.RadioButton
import androidx.annotation.RequiresApi
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.MainActivity
import com.example.ipolitician.R
import com.example.ipolitician.firebase.DataAPI
import com.example.ipolitician.structures.EV
import com.example.ipolitician.structures.PV
import com.example.ipolitician.structures.Selected
import com.example.ipolitician.structures.Voted
import com.example.ipolitician.textColor
import kotlinx.android.synthetic.main.election_holder.view.*


class ElectionRecyclerViewAdapter(private var elections: ArrayList<EV>, private var voted: Voted) : RecyclerView.Adapter<ElectionRecyclerViewHolder>() {

    private var DB = DataAPI.instance

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionRecyclerViewHolder {
        return ElectionRecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.election_holder, parent, false))
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

        for (candidate in elections[position].candidates.toList().sortedBy { (_, value) -> -value}.toMap()){
            var cand = RadioButton(holder.candidates.context)
            cand.text = candidate.key + " -> ${candidate.value}"
            cand.setTextColor(textColor.data)
            cand.buttonTintList = ColorStateList.valueOf(textColor.data)
            holder.candidates.addView(cand)
        }

        if(voted.voted.containsKey(elections[position].id)) {
            (holder.candidates.getChildAt(voted.voted[elections[position].id]!!) as RadioButton).isChecked = true
        }

        holder.candidates.setOnCheckedChangeListener { radio, _ ->
            voted.voted[elections[holder.adapterPosition].id] = radio.indexOfChild(radio.findViewById(radio.checkedRadioButtonId))

            DB.setUserElections(MainActivity.uniqueID!!, voted)
            // ++ left
            fetch_data()
        }

    }

    fun fetch_data() {
        DB.getElections { elections ->
                this.elections = elections
                notifyDataSetChanged()
        }
    }
}
