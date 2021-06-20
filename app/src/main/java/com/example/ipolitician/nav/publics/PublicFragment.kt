package com.example.ipolitician.nav.publics

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.ipolitician.R
import com.example.ipolitician.Util.dialog
import com.example.ipolitician.firebase.DataAPI
import com.example.ipolitician.nav.profile.ProfileFragment
import com.example.ipolitician.structures.EV
import com.example.ipolitician.structures.Voted
import com.example.ipolitician.textColor
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.*
import java.lang.ref.WeakReference


class PublicFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var demoCollectionAdapter: DemoCollectionAdapter
    private lateinit var viewPager: ViewPager2
    private val DB = DataAPI()

    private lateinit var swipe: SwipeRefreshLayout
    private lateinit var spinner1 : Spinner
    private lateinit var spinner2 : Spinner
    private lateinit var spinner3 : Spinner
    private var ages = arrayListOf("All")
    private var genders = arrayListOf("All")
    private var regions = arrayListOf("All")

    private var _currentPage: WeakReference<PublicFragmentPage>? = null
    private val currentPage
        get() = _currentPage?.get()
    fun setCurrentPage(page: PublicFragmentPage) {
        _currentPage = WeakReference(page)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_public_pager, container, false)
        ages.addAll(ProfileFragment.ages)
        genders.addAll(ProfileFragment.genders)
        regions.addAll(ProfileFragment.regions)

        swipe = root.findViewById(R.id.home)
        swipe.setOnRefreshListener(this)
        spinner1 = root.findViewById<Spinner>(R.id.spinner4)
        spinner2 = root.findViewById<Spinner>(R.id.spinner5)
        spinner3 = root.findViewById<Spinner>(R.id.spinner6)

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
        ProfileFragment.setSpinner(
            spinner3,
            root.context,
            regions,
        )

        root.findViewById<Button>(R.id.filterBtn).setOnClickListener {
            currentPage?.repaintGraph(spinner1.selectedItemPosition - 1, spinner2.selectedItemPosition - 1, spinner3.selectedItemPosition - 1)
            if(currentPage?.load == -1) {
                Snackbar.make(it, "No such data found", Snackbar.LENGTH_LONG).setAction(
                    "Action",
                    null
                ).show()
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        DB.getElections { elections ->
            demoCollectionAdapter = DemoCollectionAdapter(this, elections)
            viewPager = view.findViewById(R.id.viewPager)
            viewPager.adapter = demoCollectionAdapter

            val tabLayout = view.findViewById<TabLayout>(R.id.public_tablayout)

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab
                tab.text = demoCollectionAdapter.getTabTitle(position)
            }.attach()
        }
    }

    override fun onRefresh() {
        currentPage?.repaintGraph()
        swipe.isRefreshing = false
    }
}

class DemoCollectionAdapter(fragment: Fragment, private var elections: ArrayList<EV>) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = elections.size + 1

    override fun createFragment(position: Int): Fragment {
        return PublicFragmentPage.newInstance(position, if(position == 0) null else elections[position-1])
    }

    fun getTabTitle(position: Int) : String {
        return if(position == 0) "მოსახლეობის მოსაზრება" else elections[position-1].title
    }
}


private const val ARG_OBJECT = "election_data"
private const val ARG_POSITION = "position_data"

class PublicFragmentPage: Fragment() {

    private lateinit var barView: BarChart

    private val DB = DataAPI.instance
    private var chartData : MutableMap<String, Int> = mutableMapOf()
    private var chartElection : EV? = null
    private var chartPosition: Int = 0
    var load = 0

    companion object {
        fun newInstance(position: Int, election: EV?) : PublicFragmentPage {
            val fragment = PublicFragmentPage()
            fragment.arguments = Bundle().apply {
                // Our object is just an integer :-P
                putInt(ARG_POSITION, position)
                putParcelable(ARG_OBJECT, election)
            }
            return fragment
        }

        val COLORS = listOf(ColorTemplate.LIBERTY_COLORS, ColorTemplate.JOYFUL_COLORS, ColorTemplate.PASTEL_COLORS, ColorTemplate.VORDIPLOM_COLORS)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_public, container, false)
        barView = root.findViewById(R.id.bar_view)
        return root
    }

    fun repaintGraph(ageIdx: Int = -1, genderIdx: Int = -1, regionIdx: Int = -1){
        dialog.show()
        chartData.clear()
        DB.getUsers() { users, ids ->
            val idxs = ids.filterIndexed { index, _ -> (ageIdx == -1 || users[index].age == ageIdx)
                    && (genderIdx == -1 || users[index].gender == genderIdx) && (regionIdx == -1 || users[index].region == ProfileFragment.regions[regionIdx])}
            load = idxs.size
            for (id in idxs){
                if(chartElection == null) {
                    DB.getSubmission(id) { sel ->
                        if(sel.selected.isEmpty() || sel.party == "") { return@getSubmission }

                        if (chartData.containsKey(sel.party)){
                            chartData[sel.party] = chartData[sel.party]!! + 1
                        } else {
                            chartData[sel.party] = 1
                        }
                        tryDraw()
                    }
                } else {
                    DB.getUserElections(id) { voted ->
                        if(voted.voted.isEmpty()) { return@getUserElections }

                        if(voted.voted.containsKey(chartElection!!.id)) {
                            val candidate = chartElection!!.candidates[voted.voted[chartElection!!.id]!!]
                            if (chartData.containsKey(candidate)){
                                chartData[candidate] = chartData[candidate]!! + 1
                            } else {
                                chartData[candidate] = 1
                            }
                            Log.d("CHART", chartData.toString())
                            tryDraw()
                        }
                    }
                }

            }
            dialog.dismiss()
            if(chartData.isEmpty()) {
                tryDraw()
                load = -1
            }
        }
    }

    private fun tryDraw() {
        if(--load > 0) return

        var sorted = chartData.map { it }.sortedBy { -it.value }
        val sum = sorted.map { it.value }.sum()

        val datasets = sorted.mapIndexed { index, entry -> BarDataSet(mutableListOf(BarEntry(index.toFloat(), entry.value.toFloat() / sum * 100)), entry.key) }
        datasets.forEachIndexed { index, barDataSet ->
            barDataSet.valueTextSize = 16f
            barDataSet.valueTextColor = textColor.data
            barDataSet.colors = listOf(COLORS[chartPosition % COLORS.size][index % ColorTemplate.LIBERTY_COLORS.size])
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_OBJECT)}?.apply {
            chartElection = getParcelable(ARG_OBJECT)
            chartPosition = getInt(ARG_POSITION)
            Log.d("BUNDLE", "load" + chartElection.toString())
            repaintGraph()
        }
    }

    override fun onResume() {
        super.onResume()
        (parentFragment as PublicFragment).setCurrentPage(this)
    }
}