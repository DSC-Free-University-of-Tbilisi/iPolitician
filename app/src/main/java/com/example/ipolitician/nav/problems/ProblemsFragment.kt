package com.example.ipolitician.nav.problems

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.MainActivity
import com.example.ipolitician.R
import com.example.ipolitician.Util.dialog
import com.example.ipolitician.firebase.DataAPI
import com.example.ipolitician.recycler.FilterableRecyclerView
import com.example.ipolitician.recycler.ProblemsRecyclerViewAdapter
import com.example.ipolitician.search.SearchComponent
import com.example.ipolitician.structures.PV
import com.example.ipolitician.textColor


class ProblemsFragment : Fragment() {

    private lateinit var ProblemsRecyclerView: RecyclerView
    private lateinit var search: SearchComponent
    private lateinit var viewModel: ProblemsViewModel
    private val DB = DataAPI.instance

    private lateinit var upSort: Button
    private lateinit var downSort: Button
    private lateinit var totSort: Button
    val sortBstate = mutableMapOf<Int,Int>()
    val sortBcolor = mutableMapOf<Int,String>()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_problems, container, false)
//        root.foreground.alpha = 0

        ProblemsRecyclerView = root.findViewById(R.id.problems_recyclerview)
        ProblemsRecyclerView.layoutManager = LinearLayoutManager(context)

        search = root.findViewById(R.id.problemsSearch)
        search.hideKeyboardOnClose(this)
        dialog.show()
        setFromFireStore()

        root.findViewById<Button>(R.id.add_post).setOnClickListener {
            val pw = PopupWindow(inflater.inflate(R.layout.fragment_problem_post, null, false), 800, 400, true)
            pw.animationStyle = R.style.Animation
            pw.showAtLocation(root.findViewById(R.id.problem), Gravity.CENTER, 0, 0)
            pw.setBackgroundDrawable(ColorDrawable(Color.BLACK))
//            root.foreground.alpha = 220

            pw.contentView.findViewById<Button>(R.id.post_button).setOnClickListener {
                val problem = pw.contentView.findViewById<EditText>(R.id.problem_post).text.toString()
                if(problem.isNotEmpty()) {
                    DB.getProblemID() { id ->
                        DB.setProblem(PV(id=id, problem = problem, upvotes = 0, downvotes = 0))
                        (ProblemsRecyclerView.adapter as ProblemsRecyclerViewAdapter).fetch_data()
                    }
                }
//                root.foreground.alpha = 0
                pw.dismiss()
            }
        }

        upSort = root.findViewById(R.id.sort_by_up_votes)
        downSort = root.findViewById(R.id.sort_by_down_votes)
        totSort = root.findViewById(R.id.sort_by_votes)

        sortBcolor[upSort.id] = "#00ff04"
        sortBcolor[downSort.id] = "#ff0000"
        sortBcolor[totSort.id] = "#FF9800"

        setDefaultExcept()

        configureSortButtons(upSort)
        configureSortButtons(downSort)
        configureSortButtons(totSort)

        return root
    }

    private fun setDefaultExcept(button: Button? = null, df_clr: Int = textColor.data) : Int {
        var type = 0

        if(upSort != button) {
            sortBstate[upSort.id] = 0
            upSort.setTextColor(df_clr)
            upSort.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_up_down,0,0,0)
        } else type = 1

        if(downSort != button) {
            sortBstate[downSort.id] = 0
            downSort.setTextColor(df_clr)
            downSort.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_up_down,0,0,0)
        } else type = -1

        if(totSort != button) {
            sortBstate[totSort.id] = 0
            totSort.setTextColor(df_clr)
            totSort.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_up_down,0,0,0)
        }

        return type
    }

    private fun configureSortButtons(button: Button) {
        button.setOnClickListener {
            val type = setDefaultExcept(button)
            val key = it.id
            when(sortBstate[key]) {
                    0 -> {
                        button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_down,0,0,0)
                        button.setTextColor(Color.parseColor(sortBcolor[key]))
                        sortBstate[key] = 1

                        (ProblemsRecyclerView.adapter as ProblemsRecyclerViewAdapter).sortBy(type, 1)
                    }
                    1 -> {
                        button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_up,0,0,0)
                        button.setTextColor(Color.parseColor(sortBcolor[key]))
                        sortBstate[key] = -1

                        (ProblemsRecyclerView.adapter as ProblemsRecyclerViewAdapter).sortBy(type, -1)
                    }
                    else -> {
                        button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_up_down,0,0,0)
                        button.setTextColor(textColor.data)
                        sortBstate[key] = 0

                        if(sortBstate[upSort.id] == 0 && sortBstate[downSort.id] == 0 &&sortBstate[totSort.id] == 0) {
                            (ProblemsRecyclerView.adapter as ProblemsRecyclerViewAdapter).fetch_data()
                        } else {
                            (ProblemsRecyclerView.adapter as ProblemsRecyclerViewAdapter).sortBy(type, 0)
                        }
                    }
                }
            }
    }

    private fun setFromFireStore() {
        DB.getProblems() { problems ->
            DB.getUserProblems(MainActivity.uniqueID!!) { voted ->
                ProblemsRecyclerView.adapter = ProblemsRecyclerViewAdapter(problems, voted)
                search.setAdapter(ProblemsRecyclerView.adapter as FilterableRecyclerView)
                dialog.dismiss()
            }
        }
    }
}