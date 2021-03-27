package com.example.ipolitician.nav.election

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ipolitician.R

class ElectionFragment : Fragment() {

    companion object {
        fun newInstance() = ElectionFragment()
    }

    private lateinit var viewModel: ElectionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.election_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ElectionViewModel::class.java)
        // TODO: Use the ViewModel
    }

}