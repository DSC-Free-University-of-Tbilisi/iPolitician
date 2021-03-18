package com.example.ipolitician.ui.slideshow

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.ipolitician.MainActivity
import com.example.ipolitician.R
import com.example.ipolitician.structures.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SlideshowFragment : Fragment() {

    private lateinit var slideshowViewModel: SlideshowViewModel
    val FS = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)

//        val textView: TextView = root.findViewById(R.id.text_slideshow)
//        slideshowViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        val ages = arrayOf("Under 18", "18-24", "25-30", "31-40", "40-55", "56+")
        val genders = arrayOf("Male", "Female", "Non-binary/third gender")

        // access the spinner
        val spinner1 = root.findViewById<Spinner>(R.id.spinner)
        val spinner2 = root.findViewById<Spinner>(R.id.spinner2)
        if (spinner1 != null && spinner2 != null) {
            val adapter1 = ArrayAdapter(
                root.context,
                android.R.layout.simple_spinner_dropdown_item, ages
            )
            val adapter2 = ArrayAdapter(
                root.context,
                android.R.layout.simple_spinner_dropdown_item, genders
            )
            spinner1.adapter = adapter1
            spinner2.adapter = adapter2

            spinner1.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    Toast.makeText(
                        root.context,
                        "You selected " + ages[position], Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }

            spinner2.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    Toast.makeText(
                        root.context,
                        "You selected " + genders[position], Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
        val but = root.findViewById<Button>(R.id.save)

        but.setOnClickListener {
            (activity as MainActivity).uniqueID?.let { it1 ->
                FS.collection("users").document(it1)
                    .set(
                        User(
                            age = spinner1.selectedItem.toString(),
                            gender = spinner2.selectedItem.toString()
                        )
                    )
                    .addOnSuccessListener { Log.d("aeee", "gaaketa") }
                    .addOnFailureListener { Log.d("aeee", "ar gauketebia") }
            }
        }
        return root
    }
}