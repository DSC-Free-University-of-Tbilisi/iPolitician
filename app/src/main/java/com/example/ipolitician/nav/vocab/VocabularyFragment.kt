package com.example.ipolitician.nav.vocab

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.R
import com.example.ipolitician.Util.createDialog
import com.example.ipolitician.Util.dialog
import com.example.ipolitician.firebase.DataAPI
import com.example.ipolitician.recycler.VocabularyRecyclerViewAdapter
import com.example.ipolitician.search.SearchComponent
import com.example.ipolitician.structures.VocabData

class VocabularyFragment: Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchComponent
    private lateinit var adapter: VocabularyRecyclerViewAdapter
    private val DB = DataAPI.instance

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val frag = inflater.inflate(R.layout.vocabulary_fragment, container, false)
        recyclerView = frag.findViewById(R.id.vocab_recycler)
        recyclerView.layoutManager = LinearLayoutManager(context)
        searchView = frag.findViewById(R.id.searchComponent)
        searchView.hideKeyboardOnClose(this)
        return frag
    }

    private fun loadContent(){
        dialog?.show()
        DB.getVocabulary { list ->
            adapter = VocabularyRecyclerViewAdapter(list)
            recyclerView.adapter = adapter
            searchView.setAdapter(adapter)
            dialog?.dismiss()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadContent()
    }
}