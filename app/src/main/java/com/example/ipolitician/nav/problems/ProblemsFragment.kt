package com.example.ipolitician.nav.problems

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.MainActivity
import com.example.ipolitician.R
import com.example.ipolitician.firebase.DataAPI
import com.example.ipolitician.recycler.ProblemsRecyclerViewAdapter
import com.example.ipolitician.structures.PV
import com.google.android.material.snackbar.Snackbar


class ProblemsFragment : Fragment() {

    private lateinit var ProblemsRecyclerView: RecyclerView
    private lateinit var search: SearchView
    private lateinit var viewModel: ProblemsViewModel
    private val DB = DataAPI.instance

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.problems_fragment, container, false)
//        root.foreground.alpha = 0

        ProblemsRecyclerView = root.findViewById(R.id.problems_recyclerview)
        ProblemsRecyclerView.layoutManager = LinearLayoutManager(context)

        search = root.findViewById(R.id.problemsSearch)

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                callSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
//              if (searchView.isExpanded() && TextUtils.isEmpty(newText)) {
                callSearch(newText)
                //              }
                return true
            }

            fun callSearch(query: String?) {
                if (query == null) setFromFireStore()
                else (ProblemsRecyclerView.adapter as ProblemsRecyclerViewAdapter).search(query)
            }
        })

        setFromFireStore()

        root.findViewById<Button>(R.id.add_post).setOnClickListener {
            val pw = PopupWindow(inflater.inflate(R.layout.problem_post_fragment, null, false), 700, 400, true)
            pw.animationStyle = R.style.Animation
            pw.showAtLocation(root.findViewById(R.id.problem), Gravity.CENTER, 0, 0)
//            root.foreground.alpha = 220

            pw.contentView.findViewById<Button>(R.id.post_button).setOnClickListener {
                val problem = pw.contentView.findViewById<EditText>(R.id.problem_post).text.toString()
                if(problem.isNotEmpty()) {
                    DB.getProblemID() { id ->
                        DB.setProblem(PV(id=id, problem = problem, upvotes = 0, downvotes = 0))
                        (ProblemsRecyclerView.adapter as ProblemsRecyclerViewAdapter).fetch_data()
                    }
                }
                //        root.foreground.alpha = 0
                pw.dismiss()
            }
        }

        return root
    }


    private fun setFromFireStore() {
        DB.getProblems() { problems ->
            DB.getUserProblems(MainActivity.uniqueID!!) { voted ->
                ProblemsRecyclerView.adapter = ProblemsRecyclerViewAdapter(problems, voted)
            }
        }
    }
}