package com.example.ipolitician.nav.publics

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.example.ipolitician.MainActivity
import com.example.ipolitician.R
import com.example.ipolitician.nav.profile.ProfileFragment
import com.example.ipolitician.structures.Party
import com.example.ipolitician.structures.QA
import com.example.ipolitician.structures.Selected
import com.example.ipolitician.structures.User
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import java.lang.Math.round
import java.util.concurrent.CountDownLatch


class PublicFragment : Fragment() {

//    private lateinit var anyChartView : AnyChartView
    private lateinit var aaChartView: AAChartView
    private lateinit var spinner1 : Spinner
    private lateinit var spinner2 : Spinner
    private val FS = Firebase.firestore
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

//        anyChartView = root.findViewById(R.id.any_chart_view) as AnyChartView
        aaChartView = root.findViewById(R.id.aa_chart_view)
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
        FS.collection("users").get().addOnSuccessListener { documents ->
            parties.clear()
            val users: ArrayList<String> = arrayListOf()

            for (dc in documents) {
                Log.d("load", "${dc.id}")
                val usr = dc.toObject(User::class.java)
                if((usr.age == spinner1.selectedItemPosition - 1 || spinner1.selectedItemPosition <= 0) &&
                        (usr.gender == spinner2.selectedItemPosition - 1 || spinner2.selectedItemPosition <= 0)){
                    users.add(dc.id)
                }
            }
            Log.d("dbg users", users.toString())
            load = users.size
            for (usr in users){
                FS.collection("submissions").document(usr).get()
                    .addOnSuccessListener { document ->
                        if (!document.exists()){ return@addOnSuccessListener }
                        val sel = document.toObject(Selected::class.java)!!
                        if (sel.party == "") return@addOnSuccessListener

                        if (parties.containsKey(sel.party)){
                            parties[sel.party] = parties[sel.party]!! + 1
                        } else {
                            parties[sel.party] = 1
                        }
                        tryDraw()
                    }.addOnFailureListener {
                        tryDraw()
                        Log.d("listener", "fail")
                    }
            }
            if(parties.isEmpty()) { load = -1 }
        }

    }

    private fun tryDraw() {
        if(--load > 0) return

        var sorted = parties.map { it }.sortedBy { -it.value }
        val sum = sorted.map { it.value }.sum()
        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Column)
            .title("Find out how the public thinks")
            .subtitle("total votes: $sum")
            .backgroundColor("#ffffff")
            .series(sorted.map { AASeriesElement().name(it.key).data(arrayOf("%.${2}f".format(it.value.toDouble() / sum * 100).toDouble()))}.toTypedArray())
            .stacking(AAChartStackingType.False)
            .dataLabelsEnabled(true)

        aaChartView.aa_drawChartWithChartModel(aaChartModel)
    }
}