package com.example.ipolitician.nav.publics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.example.ipolitician.MainActivity
import com.example.ipolitician.R
import com.example.ipolitician.nav.profile.ProfileFragment
import com.github.aachartmodel.aainfographics.aachartcreator.*


class PublicFragment : Fragment() {

    private lateinit var homeViewModel: PublicViewModel
    private lateinit var anyChartView : AnyChartView
    private lateinit var spinner1 : Spinner
    private lateinit var spinner2 : Spinner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_public, container, false)


//        val aaChartView2 = root.findViewById<AAChartView>(R.id.aa_chart_view2)
        val aaChartModel2 : AAChartModel = AAChartModel()
            .chartType(AAChartType.Bar)
            .title("title")
            .subtitle("subtitle")
            .backgroundColor("#ffffff")
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Tokyo")
                        .data(arrayOf(30)),
                    AASeriesElement()
                        .name("NewYork")
                        .data(arrayOf(40)),
                    AASeriesElement()
                        .name("London")
                        .data(arrayOf(45)),
                    AASeriesElement()
                        .name("Berlin")
                        .data(arrayOf(60))
                )
            )
//        aaChartView2.aa_drawChartWithChartModel(aaChartModel2)
        spinner1 = root.findViewById<Spinner>(R.id.spinner4)
        spinner2 = root.findViewById<Spinner>(R.id.spinner5)
//        spinner1.onItemSelectedListener
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
        //
        val pie = AnyChart.pie()

        val data: MutableList<DataEntry> = ArrayList()
        data.add(ValueDataEntry("John", 10000))
        data.add(ValueDataEntry("Jake", 12000))
        data.add(ValueDataEntry("Peter", 18000))
        data.add(ValueDataEntry("John", 10000))

        pie.data(data)

        anyChartView = root.findViewById(R.id.any_chart_view) as AnyChartView
        anyChartView.setChart(pie)

        return root
    }

    fun repaintGraph(){
        spinner1
    }
}