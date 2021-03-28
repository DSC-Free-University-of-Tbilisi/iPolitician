package com.example.ipolitician.nav.publics

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.CountDownLatch


class PublicFragment : Fragment() {

    private lateinit var homeViewModel: PublicViewModel
    private lateinit var anyChartView : AnyChartView
    private lateinit var spinner1 : Spinner
    private lateinit var spinner2 : Spinner
    private val FS = Firebase.firestore
    private var parties : MutableMap<String, Int> = mutableMapOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_public, container, false)

        anyChartView = root.findViewById(R.id.any_chart_view) as AnyChartView
        spinner1 = root.findViewById<Spinner>(R.id.spinner4)
        spinner2 = root.findViewById<Spinner>(R.id.spinner5)
        ProfileFragment.setSpinner(
            spinner1,
            root.context,
            ProfileFragment.ages,
//            MainActivity.user!!.age
        )
        ProfileFragment.setSpinner(
            spinner2,
            root.context,
            ProfileFragment.genders,
//            MainActivity.user!!.gender
        )
        repaintGraph()
        return root
    }

    fun repaintGraph(){
        var users: ArrayList<String> = arrayListOf()
        FS.collection("users").get().addOnSuccessListener { documents ->
            // here is bug with countdownlatch
            val users: ArrayList<String> = arrayListOf()
            for (dc in documents){
                users.add(dc.id)
            }
            if (!users.contains(MainActivity.uniqueID)) return@addOnSuccessListener
            for (dc in documents) {
                Log.d("load", "${dc.id}")
                val usr = dc.toObject(User::class.java)
                if(usr.age == spinner1.selectedItemPosition && usr.gender == spinner2.selectedItemPosition){
                    users.add(dc.id)
                }
            }
            val latch = CountDownLatch(users.size)
            for (usr in users){
                FS.collection("submissions").document(usr).get()
                    .addOnSuccessListener { document ->
                        val sel = document.toObject(Selected::class.java)!!
                        parties[sel.party]?.plus(1)
                        latch.countDown()
                    }
                    .addOnFailureListener {
                        latch.countDown()
                    }
            }
            latch.await()
            val pie = AnyChart.pie()
            val data = parties.map { it }.sortedBy { -it.value }.map { ValueDataEntry(it.key, it.value) }
            pie.data(data)
            anyChartView.setChart(pie)
        }
    }


    //        val aaChartView2 = root.findViewById<AAChartView>(R.id.aa_chart_view2)
//        val aaChartModel2 : AAChartModel = AAChartModel()
//            .chartType(AAChartType.Bar)
//            .title("title")
//            .subtitle("subtitle")
//            .backgroundColor("#ffffff")
//            .series(
//                arrayOf(
//                    AASeriesElement()
//                        .name("Tokyo")
//                        .data(arrayOf(30)),
//                    AASeriesElement()
//                        .name("NewYork")
//                        .data(arrayOf(40)),
//                    AASeriesElement()
//                        .name("London")
//                        .data(arrayOf(45)),
//                    AASeriesElement()
//                        .name("Berlin")
//                        .data(arrayOf(60))
//                )
//            )
//        aaChartView2.aa_drawChartWithChartModel(aaChartModel2)
}