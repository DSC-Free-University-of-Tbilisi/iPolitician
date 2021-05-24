package com.example.ipolitician.recycler

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.R
import com.example.ipolitician.structures.QA
import com.example.ipolitician.structures.Selected
import com.example.ipolitician.structures.VocabData

class VocabularyRecyclerViewAdapter(
    private var vocab: ArrayList<VocabData>,
) : RecyclerView.Adapter<VocabularyRecyclerViewHolder>(), FilterableRecyclerView {

    private var vocabFilter = vocab.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VocabularyRecyclerViewHolder {
        return VocabularyRecyclerViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.vocabulary_holder,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return vocabFilter.size
    }

    override fun onBindViewHolder(holder: VocabularyRecyclerViewHolder, position: Int) {
        holder.header.text = vocabFilter[position].header
        holder.description.text = vocabFilter[position].description
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                vocabFilter = if (charSearch.isEmpty()) {
                    vocab
                } else {
                    vocab.filter { it.header.contains(charSearch)}
                }
                val filterResults = FilterResults()
                filterResults.values = vocabFilter
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                vocabFilter = results?.values as ArrayList<VocabData>
                notifyDataSetChanged()
            }
        }
    }

}