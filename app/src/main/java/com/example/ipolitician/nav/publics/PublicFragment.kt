package com.example.ipolitician.nav.publics

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.ipolitician.R
import com.example.ipolitician.Util.dialog
import com.example.ipolitician.firebase.DataAPI
import com.example.ipolitician.nav.profile.ProfileFragment
import com.example.ipolitician.textColor
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import kotlin.random.Random


class PublicFragment : Fragment() {


    private lateinit var barView: BarChart
    private lateinit var spinner1 : Spinner
    private lateinit var spinner2 : Spinner
    private val DB = DataAPI.instance
    private var parties : MutableMap<String, Int> = mutableMapOf()
    private var ages = arrayListOf("All")
    private var genders = arrayListOf("All")
    private var load = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_public, container, false)
        ages.addAll(ProfileFragment.ages)
        genders.addAll(ProfileFragment.genders)

        dialog.show()

        barView = root.findViewById(R.id.bar_view)

        spinner1 = root.findViewById<Spinner>(R.id.spinner4)
        spinner2 = root.findViewById<Spinner>(R.id.spinner5)
        ProfileFragment.setSpinner(
            spinner1,
            root.context,
            ages,
        )
        ProfileFragment.setSpinner(
            spinner2,
            root.context,
            genders,
        )

        root.findViewById<Button>(R.id.filterBtn).setOnClickListener {
            repaintGraph()
            if(load == -1) {
                Snackbar.make(it, "No such data found", Snackbar.LENGTH_LONG).setAction(
                    "Action",
                    null
                ).show()
            }
        }

        repaintGraph()
        return root
    }

    private fun repaintGraph(){
        DB.getUsers() { users, ids ->
            load = users.size
            for (id in ids){
                DB.getSubmission(id) { sel ->
                    if(sel.selected.isEmpty() || sel.party == "") {
                        dialog.dismiss()
                        return@getSubmission
                    }

                    if (parties.containsKey(sel.party)){
                        parties[sel.party] = parties[sel.party]!! + 1
                    } else {
                        parties[sel.party] = 1
                    }
                    tryDraw()
                }
            }
            if(parties.isEmpty()) { load = -1 }
        }
    }

    private fun tryDraw() {
        if(--load > 0) return

        var sorted = parties.map { it }.sortedBy { -it.value }
        val sum = sorted.map { it.value }.sum()




        val datasets = sorted.mapIndexed { index, entry -> BarDataSet(mutableListOf(BarEntry(index.toFloat(), entry.value.toFloat() / sum * 100)), entry.key) }
        datasets.forEachIndexed { index, barDataSet ->
            barDataSet.valueTextSize = 16f
            barDataSet.valueTextColor = textColor.data
            barDataSet.colors = listOf(ColorTemplate.VORDIPLOM_COLORS[index])
        }
        val lineData = BarData(datasets)
        lineData.setValueFormatter(PercentFormatter())

        barView.xAxis.setDrawGridLines(false)
        barView.axisLeft.setDrawGridLines(false)
        barView.axisRight.setDrawGridLines(false)
        barView.axisRight.setDrawLabels(false)
        barView.xAxis.setDrawLabels(false)
        barView.axisLeft.textColor = textColor.data

        val legend = barView.legend
        legend.textSize = 16f
        legend.textColor = textColor.data
        legend.isWordWrapEnabled = true
        legend.form = Legend.LegendForm.CIRCLE
        legend.formSize = 12f
        legend.xEntrySpace = 12f
        legend.yEntrySpace = 4f


        barView.description.isEnabled = false
        barView.isScaleXEnabled = false
        barView.isScaleYEnabled = false
        barView.data = lineData
        barView.setFitBars(true)
        barView.invalidate() // refresh
        dialog.dismiss()
    }
}