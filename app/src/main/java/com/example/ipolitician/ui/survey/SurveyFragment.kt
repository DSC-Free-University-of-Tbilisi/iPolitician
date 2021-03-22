package com.example.ipolitician.ui.survey

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.MainActivity
import com.example.ipolitician.R
import com.example.ipolitician.firebase.QA
import com.example.ipolitician.recycler.QuestionsRecyclerViewAdapter
import com.example.ipolitician.structures.Selected
import com.example.ipolitician.structures.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SurveyFragment : Fragment() {

    private lateinit var surveyViewModel: SurveyViewModel
    private lateinit var QuestionsRecyclerView: RecyclerView
    private var questions: ArrayList<QA> = arrayListOf()
    private var selected: Selected = Selected()
    private val FS = Firebase.firestore


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        surveyViewModel = ViewModelProviders.of(this).get(SurveyViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_survey, container, false)

        QuestionsRecyclerView = root.findViewById(R.id.questions_recyclerview)
        QuestionsRecyclerView.layoutManager = LinearLayoutManager(context)

        setFromFireStore()

        val fab: FloatingActionButton = (activity as MainActivity).findViewById(R.id.fab)
        fab.setOnClickListener {
            val selected = (QuestionsRecyclerView.adapter as QuestionsRecyclerViewAdapter).getSelected()
            FS.collection("submissions").document(MainActivity.uniqueID!!)
                .set(selected)
                .addOnSuccessListener { Log.d("listener", "yep") }
                .addOnFailureListener { Log.d("listener", "nope") }
            val usr = MainActivity.user
            if (usr != null) {
                MainActivity.user = User(usr.age, usr.gender)
            }
        }
        return root
    }

    private fun setFromFireStore() {
        FS.collection("questions").get().addOnSuccessListener { documents ->
            for (dc in documents) {
                Log.d("load", "${dc.id}")
                questions.add(dc.toObject(QA::class.java))
            }
            FS.collection("submissions").document(MainActivity.uniqueID!!).get().addOnSuccessListener { document ->
                selected = document.toObject(Selected::class.java)!!
                QuestionsRecyclerView.adapter =  QuestionsRecyclerViewAdapter(questions, selected)
            }
        }
    }
}