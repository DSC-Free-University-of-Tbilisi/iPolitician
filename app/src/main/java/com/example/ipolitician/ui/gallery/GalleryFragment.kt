package com.example.ipolitician.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.MainActivity
import com.example.ipolitician.R
import com.example.ipolitician.firebase.FireStore
import com.example.ipolitician.firebase.QA
import com.example.ipolitician.recycler.QuestionsRecyclerViewAdapter
import com.example.ipolitician.recycler.QuestionsRecyclerViewHolder
import com.example.ipolitician.structures.Question
import com.example.ipolitician.structures.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.question_holder.*

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private lateinit var QuestionsRecyclerView: RecyclerView
    private var questions: ArrayList<QA> = arrayListOf()
    private val FS = Firebase.firestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        galleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)

        QuestionsRecyclerView = root.findViewById(R.id.questions_recyclerview)
        QuestionsRecyclerView.layoutManager = LinearLayoutManager(context)

        setFromFireStore()

        val fab: FloatingActionButton = (activity as MainActivity).findViewById(R.id.fab)
        fab.setOnClickListener {
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
            QuestionsRecyclerView.adapter =  QuestionsRecyclerViewAdapter(questions, arrayListOf())
        }
    }
}