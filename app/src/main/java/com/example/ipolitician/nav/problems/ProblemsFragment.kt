package com.example.ipolitician.nav.problems

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.MainActivity
import com.example.ipolitician.R
import com.example.ipolitician.nav.survey.SurveyViewModel
import com.example.ipolitician.recycler.ProblemsRecyclerViewAdapter
import com.example.ipolitician.recycler.QuestionsRecyclerViewAdapter
import com.example.ipolitician.structures.PV
import com.example.ipolitician.structures.Selected
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProblemsFragment : Fragment() {

    private lateinit var ProblemsRecyclerView: RecyclerView
    private lateinit var viewModel: ProblemsViewModel
    private var problems: ArrayList<PV> = arrayListOf()
    private var selected: Selected = Selected()
    private val FS = Firebase.firestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.problems_fragment, container, false)

        ProblemsRecyclerView = root.findViewById(R.id.problems_recyclerview)
        ProblemsRecyclerView.layoutManager = LinearLayoutManager(context)

        setFromFireStore()
        return root
    }

    private fun setFromFireStore() {
        FS.collection("problems").get().addOnSuccessListener { documents ->
            for (dc in documents) {
                Log.d("load probs", dc.toString())
                problems.add(dc.toObject(PV::class.java))
            }
            selected.selected.add(-1)
            selected.selected.add(1)
            selected.selected.add(-1)
            selected.selected.add(1)
            ProblemsRecyclerView.adapter =  ProblemsRecyclerViewAdapter(problems, selected)
//            FS.collection("submissions").document(MainActivity.uniqueID!!).get().addOnSuccessListener { document ->
//                selected = document.toObject(Selected::class.java)!!
//
//            }
        }
    }
}