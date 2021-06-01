package com.example.ipolitician.nav.election

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.MainActivity
import com.example.ipolitician.R
import com.example.ipolitician.Util.dialog
import com.example.ipolitician.firebase.DataAPI
import com.example.ipolitician.recycler.ElectionRecyclerViewAdapter

class ElectionFragment : Fragment() {

    private lateinit var ElectionsRecyclerView: RecyclerView
    private lateinit var noData: TextView
    private var DB = DataAPI.instance

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_election, container, false)

        noData = root.findViewById(R.id.textView13)
        ElectionsRecyclerView = root.findViewById(R.id.elections_recyclerview)
        ElectionsRecyclerView.layoutManager = LinearLayoutManager(context)
        dialog.show()
        setData()

        return root
    }

    private fun setData() {
        DB.getElections { elections ->
            DB.getUserElections(MainActivity.uniqueID!!) { voted ->
                ElectionsRecyclerView.adapter = ElectionRecyclerViewAdapter(elections, voted)
                noData.visibility = if (elections.isNotEmpty()) View.INVISIBLE else View.VISIBLE
                dialog.dismiss()
            }
        }
    }

}