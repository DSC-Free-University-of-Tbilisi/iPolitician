package com.example.ipolitician.nav.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.ipolitician.MainActivity
import com.example.ipolitician.R
import com.example.ipolitician.Util.md5
import com.example.ipolitician.firebase.DataAPI
import com.example.ipolitician.structures.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ProfileFragment : Fragment() {

    private lateinit var slideshowViewModel: ProfileViewModel
    private val DB = DataAPI.instance
    private lateinit var spinner1: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var spinner3: Spinner

    companion object {
        val ages = arrayListOf("Under 18", "18-24", "25-30", "31-40", "40-55", "56+")
        val genders = arrayListOf("Male", "Female", "Non-binary/third gender")
        val regions = arrayListOf("coming soon - app feature")

        fun setSpinner(spinner: Spinner, context: Context, arr: ArrayList<String>, position: Int = 0){
            if (spinner != null) {
                val adapter = ArrayAdapter(
                    context,
                    R.layout.spinner_item,
                    arr
                )
                spinner.adapter = adapter
                spinner.setSelection(position)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)


//        val textView: TextView = root.findViewById(R.id.text_slideshow)
//        slideshowViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        // access the spinner
        spinner1 = root.findViewById(R.id.spinner)
        spinner2 = root.findViewById(R.id.spinner2)
        spinner3 = root.findViewById(R.id.spinner3)

        setSpinner(spinner1, root.context, ages, MainActivity.user!!.age)
        setSpinner(spinner2, root.context, genders, MainActivity.user!!.gender)
        setSpinner(spinner3, root.context, regions)

        val but = root.findViewById<Button>(R.id.save)

        setBtnListener(but, onClick = {
            MainActivity.uniqueID?.let { it1 ->
                val usr = User(
                    password = MainActivity.user!!.password.md5(),
                    phoneNumber = MainActivity.user!!.phoneNumber,
                    age = spinner1.selectedItemPosition,
                    gender = spinner2.selectedItemPosition,
                )
                MainActivity.user = usr
                DB.setUser(it1, usr)
                findNavController().navigateUp()
                findNavController().navigate(R.id.nav_public)
            }
        })
        return root
    }


    private fun setBtnListener(button: Button, onClick: () -> Unit){
        button.setOnClickListener {
            onClick()
            Snackbar.make(it, "Profile saved successfully.", Snackbar.LENGTH_LONG).setAction(
                "Action",
                null
            ).show()
        }
    }
}