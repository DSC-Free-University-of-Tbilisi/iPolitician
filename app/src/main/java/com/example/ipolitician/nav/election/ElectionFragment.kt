package com.example.ipolitician.nav.election

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.MainActivity
import com.example.ipolitician.R
import com.example.ipolitician.firebase.DataAPI
import com.example.ipolitician.recycler.ElectionRecyclerViewAdapter
import com.example.ipolitician.structures.EV

class ElectionFragment : Fragment() {

    private lateinit var ElectionsRecyclerView: RecyclerView
    private var DB = DataAPI.instance

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.election_fragment, container, false)

        ElectionsRecyclerView = root.findViewById(R.id.elections_recyclerview)
        ElectionsRecyclerView.layoutManager = LinearLayoutManager(context)

        setData()

        return root
    }

    private fun setData() {
        DB.getElections { elections ->
            DB.getUserElections(MainActivity.uniqueID!!) { voted ->
                ElectionsRecyclerView.adapter = ElectionRecyclerViewAdapter(elections, voted)
            }
        }
    }

}