package com.example.ipolitician.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.R
import com.example.ipolitician.recycler.QuestionsRecyclerViewAdapter
import com.example.ipolitician.structures.Question
import kotlinx.android.synthetic.main.question_holder.*

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private lateinit var QuestionsRecyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        galleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)

        QuestionsRecyclerView = root.findViewById(R.id.questions_recyclerview)
        QuestionsRecyclerView.layoutManager = LinearLayoutManager(context)

        var Q1 = Question(id=0, question="Q1")
        var Q2 = Question(id=0, question="Q2")
        var questions = ArrayList<Question>()
        questions.add(Q1)
        questions.add(Q2)
        QuestionsRecyclerView.adapter =  QuestionsRecyclerViewAdapter(questions)


        return root
    }
}