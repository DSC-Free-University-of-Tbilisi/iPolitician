package com.example.ipolitician.nav.problems

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.R
import com.example.ipolitician.nav.survey.SurveyViewModel
import com.example.ipolitician.recycler.ProblemsRecyclerViewAdapter
import com.example.ipolitician.structures.PV
import com.example.ipolitician.structures.Selected

class ProblemsFragment : Fragment() {

    private lateinit var ProblemsRecyclerView: RecyclerView
    private lateinit var viewModel: ProblemsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.problems_fragment, container, false)

        ProblemsRecyclerView = root.findViewById(R.id.problems_recyclerview)
        ProblemsRecyclerView.layoutManager = LinearLayoutManager(context)

        var pv0 = PV(0,"    უნდა იყოს თუ არა დამსაქმებელი კანონით ვალდებული, რომ თანამშრომელ მამაკაცსა და ქალს გადაუხადოს მსგავსი ხელფასი, თუ ისინი ერთნაირ სამუშაოს ასრულებენ?", 20,4)
        var pv1 = PV(0,"    ემხრობით თუ არა სახელმწიფოს მიერ სოფლის მეურნეობის სუბსიდირებას?", 4,44)
        var pvs: ArrayList<PV> = arrayListOf(pv0, pv1)

        var sel: Selected = Selected()
        sel.selected.add(1)
        sel.selected.add(-1)
        ProblemsRecyclerView.adapter = ProblemsRecyclerViewAdapter(pvs, sel)
        return root
    }

}