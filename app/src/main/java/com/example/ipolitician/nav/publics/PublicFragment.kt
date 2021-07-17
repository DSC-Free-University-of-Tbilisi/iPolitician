package com.example.ipolitician.nav.publics

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.ipolitician.R
import com.example.ipolitician.Util.dialog
import com.example.ipolitician.firebase.DataAPI
import com.example.ipolitician.nav.profile.ProfileFragment
import com.example.ipolitician.structures.EV
import com.example.ipolitician.textColor
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_public.*
import kotlinx.coroutines.*
import java.lang.ref.WeakReference
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


class PublicFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var demoCollectionAdapter: DemoCollectionAdapter
    private lateinit var viewPager: ViewPager2
    private val DB = DataAPI()

    private lateinit var swipe: SwipeRefreshLayout
    private lateinit var ageFrom : EditText
    private lateinit var ageTo : EditText
    private lateinit var spinner2 : Spinner
    private lateinit var spinner3 : Spinner
    private var genders = arrayListOf("ყველა")
    private var regions = arrayListOf("ყველა")

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
        genders.addAll(ProfileFragment.genders)
        regions.addAll(ProfileFragment.regions)

        swipe = root.findViewById(R.id.home)
        swipe.setOnRefreshListener(this)
        ageFrom = root.findViewById(R.id.ageFrom)
        ageTo = root.findViewById(R.id.ageTo)
        spinner2 = root.findViewById<Spinner>(R.id.spinner5)
        spinner3 = root.findViewById<Spinner>(R.id.spinner6)

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
            val from = if(ageFrom.text.isEmpty()) 0 else ageFrom.text.toString().toInt()
            val to = if(ageTo.text.isEmpty()) 1000 else ageTo.text.toString().toInt()
            if(from > to) {
                Snackbar.make(it, "მიუთითეთ სწორი ასაკობრივი შეზღუდვა", Snackbar.LENGTH_LONG).setAction(
                    "Action",
                    null
                ).show()
                return@setOnClickListener
            }

            currentPage?.repaintGraph(from, to, spinner2.selectedItemPosition - 1, spinner3.selectedItemPosition - 1)
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
    private lateinit var description: TextView

    private val DB = DataAPI.instance
    private var chartData : MutableMap<String, Int> = mutableMapOf()
    private var chartElection : EV? = null
    private var chartPosition: Int = 0

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

        val COLORS = listOf(ColorTemplate.JOYFUL_COLORS, ColorTemplate.PASTEL_COLORS, ColorTemplate.VORDIPLOM_COLORS, ColorTemplate.LIBERTY_COLORS)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_public, container, false)
        barView = root.findViewById(R.id.bar_view)
        description = root.findViewById(R.id.chart_description)
        return root
    }

    fun repaintGraph(ageFrom: Int = 0, ageTo: Int = 1000, genderIdx: Int = -1, regionIdx: Int = -1){
        dialog.show()
        chartData.clear()
        DB.getUsers() { users, ids ->
            val idxs = ids.filterIndexed { index, _ -> (users[index].age in ageFrom..ageTo)
                    && (genderIdx == -1 || users[index].gender == genderIdx) && (regionIdx == -1 || users[index].region == ProfileFragment.regions[regionIdx])}

            val latch = CountDownLatch(idxs.size)

            GlobalScope.launch {
                for (id in idxs){

                    if(chartElection == null) {
                        DB.getSubmission(id) { sel ->
                            latch.countDown()
                            if(sel.selected.isEmpty() || sel.party == "") { return@getSubmission }

                            if (chartData.containsKey(sel.party)){
                                chartData[sel.party] = chartData[sel.party]!! + 1
                            } else {
                                chartData[sel.party] = 1
                            }

                        }
                    } else {
                        DB.getUserElections(id) { voted ->
                            latch.countDown()
                            if(voted.voted.isEmpty()) { return@getUserElections }

                            if(voted.voted.containsKey(chartElection!!.id)) {
                                val candidate = chartElection!!.candidates[voted.voted[chartElection!!.id]!!]
                                if (chartData.containsKey(candidate)){
                                    chartData[candidate] = chartData[candidate]!! + 1
                                } else {
                                    chartData[candidate] = 1
                                }
                                Log.d("CHART", chartData.toString())
                            }
                        }
                    }
                }
                latch.await(5, TimeUnit.SECONDS)
                CoroutineScope(Dispatchers.Main).async {
                    tryDraw()
                    dialog.dismiss()
                }
            }
        }
    }

    private fun tryDraw() {
        var sorted = chartData.map { it }.sortedBy { -it.value }
        val sum = sorted.map { it.value }.sum()

        val datasets = sorted.mapIndexed { index, entry -> BarDataSet(mutableListOf(BarEntry(index.toFloat(), entry.value.toFloat() / sum * 100)), entry.key) }
        datasets.forEachIndexed { index, barDataSet ->
            barDataSet.valueTextSize = 16f
            barDataSet.valueTextColor = textColor.data
            barDataSet.colors = listOf(COLORS[chartPosition % COLORS.size][index % ColorTemplate.LIBERTY_COLORS.size])
        }

        if(datasets.isEmpty()){
            barView.clear()
            return
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

        description.text = "სულ ხმები: $sum"
        barView.axisLeft.axisMinimum = 0F
        barView.axisLeft.axisMaximum = 120F

        barView.setNoDataText("ასეთი მონაცემები არ მოიძებნა")
        barView.description.isEnabled = false
        barView.isScaleXEnabled = false
        barView.isScaleYEnabled = false
        barView.data = lineData
        barView.setFitBars(true)
        barView.invalidate() // refresh
        barView.notifyDataSetChanged()
        dialog.dismiss()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_OBJECT)}?.apply {
            chartElection = getParcelable(ARG_OBJECT)
            chartPosition = getInt(ARG_POSITION)
            repaintGraph()
        }
    }

    override fun onResume() {
        super.onResume()
        (parentFragment as PublicFragment).setCurrentPage(this)
    }
}