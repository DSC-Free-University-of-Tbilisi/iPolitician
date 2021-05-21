package com.example.ipolitician.nav.problems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.MainActivity
import com.example.ipolitician.R
import com.example.ipolitician.firebase.DataAPI
import com.example.ipolitician.recycler.ProblemsRecyclerViewAdapter
import com.google.android.material.snackbar.Snackbar


class ProblemsFragment : Fragment() {

    private lateinit var ProblemsRecyclerView: RecyclerView
    private lateinit var search: SearchView
    private lateinit var viewModel: ProblemsViewModel
    private val DB = DataAPI()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.problems_fragment, container, false)

        ProblemsRecyclerView = root.findViewById(R.id.problems_recyclerview)
        ProblemsRecyclerView.layoutManager = LinearLayoutManager(context)
        root.findViewById<Button>(R.id.add_post).setOnClickListener {
            Snackbar.make(it, "Post what's in your mind - app feature", Snackbar.LENGTH_LONG).setAction(
                "Action",
                null
            ).show()
        }
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
                    if(query == null) setFromFireStore()
                    else (ProblemsRecyclerView.adapter as ProblemsRecyclerViewAdapter).search(query)
                }
            })

        setFromFireStore()
        return root
    }

    private fun setFromFireStore() {
        DB.getProblems() { problems ->
            DB.getUserProblems(MainActivity.uniqueID!!) { voted ->
                ProblemsRecyclerView.adapter =  ProblemsRecyclerViewAdapter(problems, voted)
            }
        }
    }
}