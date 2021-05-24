package com.example.ipolitician.search

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.ipolitician.R
import com.example.ipolitician.hideKeyboard
import com.example.ipolitician.recycler.FilterableRecyclerView
import com.example.ipolitician.recycler.VocabularyRecyclerViewAdapter

class SearchComponent @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    private val defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {
    val search: EditText
    init {
        LayoutInflater.from(context)
            .inflate(R.layout.custom_search_view, this, true)
        search = findViewById<EditText>(R.id.search)

    }

    fun hideKeyboardOnClose(fragment: Fragment){
        search.setOnFocusChangeListener { _, b ->
            if (!b) {
                fragment.hideKeyboard()
            }
        }
    }

    fun setAdapter(recyclerViewAdapter: FilterableRecyclerView){
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                recyclerViewAdapter.filter.filter(p0.toString())
            }
        })
    }
}