package com.example.ipolitician.nav.problems

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.MainActivity
import com.example.ipolitician.R
import com.example.ipolitician.Util.dialog
import com.example.ipolitician.firebase.DataAPI
import com.example.ipolitician.hideKeyboard
import com.example.ipolitician.recycler.FilterableRecyclerView
import com.example.ipolitician.recycler.ProblemsRecyclerViewAdapter
import com.example.ipolitician.search.SearchComponent
import com.example.ipolitician.structures.PV
import com.example.ipolitician.textColor
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import java.util.concurrent.TimeUnit


class ProblemsFragment : Fragment() {

    private lateinit var ProblemsRecyclerView: RecyclerView
    private lateinit var search: SearchComponent
    private lateinit var blurView: View
    private val DB = DataAPI.instance

    private lateinit var upSort: Button
    private lateinit var downSort: Button
    private lateinit var totSort: Button
    val sortByState = mutableMapOf<Int,Int>()
    val sortByColor = mutableMapOf<Int,Int>()

    private val PROBLEM_DELAY = 7

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_problems, container, false)
        blurView = root.findViewById(R.id.blurViewProb)
        blurView.background.alpha = 220
//        root.foreground.alpha = 0

        ProblemsRecyclerView = root.findViewById(R.id.problems_recyclerview)
        ProblemsRecyclerView.layoutManager = LinearLayoutManager(context)

        search = root.findViewById(R.id.problemsSearch)
        search.hideKeyboardOnClose(this)
        dialog.show()
        setFromFireStore()

        root.findViewById<Button>(R.id.add_post).setOnClickListener { itView ->
            DB.getUserTimestamp(MainActivity.uniqueID!!) {timestamp ->
                val now = Timestamp.now().toDate().time
                val bfr = timestamp?.timestamp?.toDate()?.time

                if(bfr == null || TimeUnit.DAYS.convert(now - bfr, TimeUnit.MILLISECONDS) > PROBLEM_DELAY) {
                    addPost(root, inflater)
                } else {
                    val days = PROBLEM_DELAY - TimeUnit.DAYS.convert(now - bfr, TimeUnit.MILLISECONDS)
                    Snackbar.make(itView, "????????????????????? ?????????????????? ?????????????????? ?????????????????????????????? $days ???????????????", Snackbar.LENGTH_LONG).setAction(
                        "Action",
                        null
                    ).show()
                }
            }
        }

        upSort = root.findViewById(R.id.sort_by_up_votes)
        downSort = root.findViewById(R.id.sort_by_down_votes)
        totSort = root.findViewById(R.id.sort_by_votes)

        sortByColor[upSort.id] = ContextCompat.getColor(root.context, R.color.upVoteClr)
        sortByColor[downSort.id] = ContextCompat.getColor(root.context, R.color.downVoteClr)
        sortByColor[totSort.id] = ContextCompat.getColor(root.context, R.color.totVoteClr)

        setDefaultExcept()

        configureSortButtons(upSort)
        configureSortButtons(downSort)
        configureSortButtons(totSort)

        return root
    }

    private fun addPost(root: View, inflater: LayoutInflater) {
        val width: Int = (Resources.getSystem().displayMetrics.widthPixels * 0.75).toInt()
        val height: Int = (Resources.getSystem().displayMetrics.heightPixels * 0.4).toInt()
        val pw = PopupWindow(inflater.inflate(R.layout.fragment_problem_post, null, false), width, height, true)
        pw.animationStyle = R.style.Animation
        blurView.visibility = View.VISIBLE
        pw.showAtLocation(root.findViewById(R.id.problem), Gravity.CENTER, 0, 0)

        pw.contentView.findViewById<Button>(R.id.post_button1).setOnClickListener {
            pw.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            val reg_pw = PopupWindow(inflater.inflate(R.layout.choose_region, null, false), width, ViewGroup.LayoutParams.WRAP_CONTENT, true)
            reg_pw.animationStyle = R.style.Animation
            reg_pw.showAtLocation(root.findViewById(R.id.problem), Gravity.CENTER, 0, 0)

            reg_pw.contentView.findViewById<Button>(R.id.post_button2).setOnClickListener {

                val editText = pw.contentView.findViewById<EditText>(R.id.problem_post)
                editText.inputType = InputType.TYPE_NULL;
                val problem = editText.text.toString()

                if(problem.isNotEmpty()) {
                    val regs = reg_pw.contentView.findViewById<RadioGroup>(R.id.regions)
                    val region = if (regs.checkedRadioButtonId >= 0) regs.findViewById<RadioButton>(regs.checkedRadioButtonId).text.toString() else ""

                    DB.getProblemID() { id ->
                        DB.setProblem(MainActivity.uniqueID!!, PV(id=id, problem = problem, upvotes = 0, downvotes = 0, region = region))
                        (ProblemsRecyclerView.adapter as ProblemsRecyclerViewAdapter).fetch_data()
                    }
                }
                pw.dismiss()
                reg_pw.dismiss()
            }
        }
        pw.setOnDismissListener { blurView.visibility = View.GONE }
    }

    private fun setDefaultExcept(button: Button? = null, df_clr: Int = textColor.data) : Int {
        var type = 0

        if(upSort != button) {
            sortByState[upSort.id] = 0
            upSort.setTextColor(df_clr)
            upSort.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_up_down,0,0,0)
        } else type = 1

        if(downSort != button) {
            sortByState[downSort.id] = 0
            downSort.setTextColor(df_clr)
            downSort.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_up_down,0,0,0)
        } else type = -1

        if(totSort != button) {
            sortByState[totSort.id] = 0
            totSort.setTextColor(df_clr)
            totSort.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_up_down,0,0,0)
        }

        return type
    }

    private fun configureSortButtons(button: Button) {
        button.setOnClickListener {
            val type = setDefaultExcept(button)
            val key = it.id
            when(sortByState[key]) {
                    0 -> {
                        button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_down,0,0,0)
                        button.setTextColor(sortByColor[key]!!)
                        sortByState[key] = 1

                        (ProblemsRecyclerView.adapter as ProblemsRecyclerViewAdapter).sortBy(type, 1)
                    }
                    1 -> {
                        button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_up,0,0,0)
                        button.setTextColor(sortByColor[key]!!)
                        sortByState[key] = -1

                        (ProblemsRecyclerView.adapter as ProblemsRecyclerViewAdapter).sortBy(type, -1)
                    }
                    else -> {
                        button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_up_down,0,0,0)
                        button.setTextColor(textColor.data)
                        sortByState[key] = 0

                        if(sortByState[upSort.id] == 0 && sortByState[downSort.id] == 0 && sortByState[totSort.id] == 0) {
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