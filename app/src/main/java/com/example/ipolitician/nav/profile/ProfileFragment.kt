package com.example.ipolitician.nav.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.ipolitician.MainActivity
import com.example.ipolitician.R
import com.example.ipolitician.firebase.DataAPI
import com.example.ipolitician.structures.User
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.collections.ArrayList


class ProfileFragment : Fragment() {

    private lateinit var slideshowViewModel: ProfileViewModel
    private val DB = DataAPI.instance
    private lateinit var spinner: Spinner

    companion object {
        val ages = arrayListOf("Under 18", "18-24", "25-30", "31-40", "40-55", "56+")
        val genders = arrayListOf("Male", "Female", "Non-binary/third gender")
        val regions = arrayListOf("აბხაზეთი", "აჭარა", "გურია", "იმერეთი", "კახეთი", "ქვემო ქართლი", "მცხეთა-მთიანეთი",
            "რაჭა-ლეჩხუმი და ქვემო სვანეთი", "სამცხე-ჯავახეთი", "შიდა ქართლი", "სამეგრელო ზემო სვანეთი", "თბილისი")

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

        fun getAge(date: String) : Int {
            val dob: Calendar = Calendar.getInstance()
            val today: Calendar = Calendar.getInstance()

            val year = date.split(date[2])[2].toInt()
            val month = date.split(date[2])[1].toInt()
            val day = date.split(date[2])[0].toInt()

            dob.set(year, month, day)
            var age: Int = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
            if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                age--
            }
            return age
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

        spinner = root.findViewById(R.id.spinner)
        setSpinner(spinner, root.context, genders, MainActivity.user!!.gender)

        val but = root.findViewById<Button>(R.id.save)

        setBtnListener(but, onClick = {
            MainActivity.uniqueID?.let { it1 ->
                val usr = User(
                    password = MainActivity.user!!.password,
                    phoneNumber = MainActivity.user!!.phoneNumber,
                    age = getAge(MainActivity.user!!.optional[2]),
                    gender = spinner.selectedItemPosition,
                    region = MainActivity.user!!.region,
                    optional = MainActivity.user!!.optional
                )
                MainActivity.user = usr
                DB.updateUser(it1, usr)
                findNavController().navigateUp()
                findNavController().navigate(R.id.nav_public)
            }
        })

        root.findViewById<TextView>(R.id.region).text = MainActivity.user!!.region

        val fullName = MainActivity.user!!.optional[0] + " " + MainActivity.user!!.optional[1]
        root.findViewById<TextView>(R.id.full_name).text = fullName

        val fullAge = MainActivity.user!!.optional[2] + " - Age: " + MainActivity.user!!.age
        root.findViewById<TextView>(R.id.birth_date).text = fullAge

        root.findViewById<TextView>(R.id.address).text = MainActivity.user!!.optional[3]

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