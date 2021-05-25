package com.example.ipolitician.recycler


import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.MainActivity
import com.example.ipolitician.R
import com.example.ipolitician.firebase.DataAPI
import com.example.ipolitician.structures.EV
import com.example.ipolitician.structures.PV
import com.example.ipolitician.structures.Selected
import com.example.ipolitician.structures.Voted
import kotlinx.android.synthetic.main.election_holder.view.*


class ElectionRecyclerViewAdapter(private var elections: ArrayList<EV>) : RecyclerView.Adapter<ElectionRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionRecyclerViewHolder {
        return ElectionRecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.election_holder, parent, false))
    }

    override fun getItemCount(): Int {
        return elections.size + 10
    }

    override fun onBindViewHolder(holder: ElectionRecyclerViewHolder, position: Int) {
        holder.itemView.election_title.text = "Election"

    }




}
