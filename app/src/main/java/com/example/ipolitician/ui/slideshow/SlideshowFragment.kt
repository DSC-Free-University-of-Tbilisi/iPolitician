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
import java.text.FieldPosition


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

        // access the spinner
        val spinner1 = root.findViewById<Spinner>(R.id.spinner)
        val spinner2 = root.findViewById<Spinner>(R.id.spinner2)

        setSpinner(spinner1, root.context, ages, MainActivity.user!!.age)
        setSpinner(spinner2, root.context, genders, MainActivity.user!!.gender)

        val but = root.findViewById<Button>(R.id.save)

        setBtnListener(but, onClick = {
            MainActivity.uniqueID?.let { it1 ->
                val usr = User(
                    age = spinner1.selectedItemPosition,
                    gender = spinner2.selectedItemPosition
                )
                MainActivity.user = usr
                FS.collection("users").document(it1)
                    .set(usr)
                    .addOnSuccessListener { Log.d("aeee", "gaaketa") }
                    .addOnFailureListener { Log.d("aeee", "ar gauketebia") }
            }
        })

        return root
    }

    companion object {
        val ages = arrayOf("Under 18", "18-24", "25-30", "31-40", "40-55", "56+")
        val genders = arrayOf("Male", "Female", "Non-binary/third gender")

        fun setSpinner(spinner: Spinner, context: Context, arr: Array<String>, position: Int = 0){
            if (spinner != null) {
                val adapter = ArrayAdapter(
                    context,
                    android.R.layout.simple_spinner_dropdown_item, arr
                )
                spinner.adapter = adapter
                spinner.setSelection(position)
            }
        }

        fun setBtnListener(button: Button, onClick: () -> Unit){
            button.setOnClickListener {
                onClick()
            }
        }
    }
}