package com.example.ipolitician.nav.survey

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.MainActivity
import com.example.ipolitician.R
import com.example.ipolitician.firebase.DataAPI
import com.example.ipolitician.recycler.QuestionsRecyclerViewAdapter
import com.example.ipolitician.structures.Party
import com.example.ipolitician.structures.QA
import com.example.ipolitician.structures.Selected
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SurveyFragment : Fragment() {

    private lateinit var surveyViewModel: SurveyViewModel
    private lateinit var QuestionsRecyclerView: RecyclerView
    private var questions: ArrayList<QA> = arrayListOf()
    private var selected: Selected = Selected()
    private lateinit var fab: FloatingActionButton
    private lateinit var aaChartView: AAChartView
    private lateinit var reSurvey: Button
    private lateinit var surveyTitle: TextView
    private var chartArray: ArrayList<Party> = arrayListOf()
    private val DB = DataAPI.instance


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        surveyViewModel = ViewModelProviders.of(this).get(SurveyViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_survey, container, false)

        QuestionsRecyclerView = root.findViewById(R.id.questions_recyclerview)
        QuestionsRecyclerView.layoutManager = LinearLayoutManager(context)
        reSurvey = root.findViewById(R.id.reSurvey)
        aaChartView = root.findViewById(R.id.aa_chart_view)
        fab = (activity as MainActivity).findViewById(R.id.fab)
        surveyTitle = root.findViewById(R.id.textView7)
        root.findViewById<Button>(R.id.reSurvey).setOnClickListener {
            configureFragment(root, true)
        }
        setFromFireStore(root)
        configureFloatingButton(root)
        return root
    }
    private fun configureFragment(root: View, isFirst: Boolean){
        if (isFirst){
            QuestionsRecyclerView.adapter =  QuestionsRecyclerViewAdapter(questions, selected)
        } else {
            configureChart(root)
        }
        configureVisibility(isFirst)
    }

    private fun configureFloatingButton(root: View){
        fab.setOnClickListener {
            selected = (QuestionsRecyclerView.adapter as QuestionsRecyclerViewAdapter).getSelected()

            configureFragment(root, false)

            Snackbar.make(it, "Survey submitted successfully.", Snackbar.LENGTH_LONG).setAction(
                "Action",
                null
            ).show()

            selected.party = chartArray[0].displayName

            DB.setSubmission(MainActivity.uniqueID!!, selected)
        }
    }

    private fun configureChart(root: View) {
        chartArray.sortBy { -calculateCompatibility(it.selected) }
        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Column)
            .title("Parties you match the most")
            .subtitle("remember your vote matters")
            .backgroundColor("#ffffff")
            .polar(true)
            .series( chartArray.map{ AASeriesElement().name(it.displayName).data(arrayOf(calculateCompatibility(it.selected))) }.toTypedArray() )


        aaChartView.aa_drawChartWithChartModel(aaChartModel)
    }

    private fun calculateCompatibility(selected: ArrayList<Int>): Int{
        var common = 0.0
        selected.forEachIndexed { index, answerIdx ->
            common += if(this.selected.selected[index] == answerIdx) 1 else 0
        }
        return (common / selected.size * 100).toInt()
    }

    private fun configureVisibility(first: Boolean){
        QuestionsRecyclerView.visibility = if (first) View.VISIBLE else View.INVISIBLE
        aaChartView.visibility = if (first) View.INVISIBLE else View.VISIBLE
        fab.visibility = if (first) View.VISIBLE else View.GONE
        reSurvey.visibility = if (first) View.INVISIBLE else View.VISIBLE
        surveyTitle.visibility = if (first) View.VISIBLE else View.INVISIBLE
    }

    override fun onDetach() {
        super.onDetach()
        fab.visibility = View.GONE
    }

    private fun setFromFireStore(root: View) {
        DB.getQuestions { questions ->
            this.questions = questions
            DB.getSubmission(MainActivity.uniqueID!!) { selected ->
                this.selected = selected
                configureFragment(root, !selected.selected.any { it != -1 })
            }
        }

        DB.getParties() { parties ->
            for(p in parties) {
                chartArray.add(p)
            }
        }
    }
}