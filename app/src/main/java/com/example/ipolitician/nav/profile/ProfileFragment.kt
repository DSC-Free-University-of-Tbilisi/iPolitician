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
import com.google.type.LatLng

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
        val geoLocs = mapOf("თბილისი" to listOf(getLatLng(41.714831, 44.686803),
                                                getLatLng(41.807827, 44.715138),
                                                getLatLng(41.809704, 44.838550),
                                                getLatLng(41.781069, 44.817142),
                                                getLatLng(41.704959, 44.893960),
                                                getLatLng(41.695087, 45.014224),
                                                getLatLng(41.637702, 44.931110),
                                                getLatLng(41.689445, 44.696248)),
            "აბხაზეთი" to listOf(getLatLng(43.390644, 40.019323),
                                                getLatLng(43.562049, 40.107214),
                                                getLatLng(43.546125, 40.653784),
                                                getLatLng(43.182715, 42.098486),
                                                getLatLng(42.986131, 41.859533),
                                                getLatLng(42.429098, 41.557409),
                                                getLatLng(43.071700, 40.879503)),
            "სამეგრელო ზემო სვანეთი" to listOf(getLatLng(43.182715, 42.098486),
                                                getLatLng(42.986131, 41.859533),
                                                getLatLng(42.429098, 41.557409),
                                                getLatLng(42.097078, 41.706409),
                                                getLatLng(42.121880, 42.245272),
                                                getLatLng(42.589841, 42.561903),
                                                getLatLng(42.919084, 42.347538),
                                                getLatLng(42.957958, 43.130266)),
            "გურია" to listOf(getLatLng(42.097078, 41.706409),
                                getLatLng(42.121880, 42.245272),
                                getLatLng(41.841130, 42.658258),
                                getLatLng(41.805680, 42.474582),
                                getLatLng(41.907054, 41.775554)),
            "აჭარა" to listOf(getLatLng(41.805680, 42.474582),
                            getLatLng(41.907054, 41.775554),
                            getLatLng(41.511892, 41.546925),
                            getLatLng(41.434325, 42.512671),
                            getLatLng(41.591591, 42.587079)),
            "რაჭა-ლეჩხუმი და ქვემო სვანეთი" to listOf(getLatLng(42.589841, 42.561903),
                                                    getLatLng(42.919084, 42.347538),
                                                    getLatLng(42.957958, 43.130266),
                                                    getLatLng(42.736490, 43.786364),
                                                    getLatLng(42.447424, 43.564070),
                                                    getLatLng(42.409088, 42.967823)),
            "იმერეთი" to listOf(getLatLng(42.447424, 43.564070),
                                getLatLng(42.409088, 42.967823),
                                getLatLng(42.589841, 42.561903),
                                getLatLng(42.121880, 42.245272),
                                getLatLng(41.841130, 42.658258),
                                getLatLng(41.942867, 43.431246)),
            "სამცხე-ჯავახეთი" to listOf(getLatLng(41.598800, 42.608584),
                                        getLatLng(41.805680, 42.474582),
                                        getLatLng(41.925322, 43.468982),
                                        getLatLng(41.767898, 43.729366),
                                        getLatLng(41.192574, 43.940692),
                                        getLatLng(41.154226, 43.472756)),
            "შიდა ქართლი" to listOf(getLatLng(42.726032, 43.780311),
                                    getLatLng(42.566427, 43.597287),
                                    getLatLng(41.947779, 43.452001),
                                    getLatLng(41.752416, 44.472780),
                                    getLatLng(42.037530, 44.485988),
                                    getLatLng(42.052943, 44.244473),
                                    getLatLng(42.615046, 44.253907)),
            "ქვემო ქართლი" to listOf(getLatLng(41.208907, 44.005079),
                                    getLatLng(41.548697, 43.720166),
                                    getLatLng(41.748904, 43.865453),
                                    getLatLng(41.750311, 44.523959),
                                    getLatLng(41.628321, 45.026417),
                                    getLatLng(41.702917, 45.016431),
                                    getLatLng(41.841706, 44.938362),
                                    getLatLng(41.834943, 45.064543),
                                    getLatLng(41.752281, 45.169827),
                                    getLatLng(41.474016, 45.286931),
                                    getLatLng(41.218819, 44.700939)),
            "მცხეთა-მთიანეთი" to listOf(getLatLng(42.685750, 44.253322),
                                        getLatLng(42.672426, 45.320395),
                                        getLatLng(41.926763, 45.093840),
                                        getLatLng(41.821967, 44.710965),
                                        getLatLng(41.698382, 44.654286),
                                        getLatLng(41.763457, 44.485339),
                                        getLatLng(42.036859, 44.530028),
                                        getLatLng(42.055928, 44.282618),
                                        getLatLng(42.352829, 44.365780),
                                        getLatLng(41.474016, 45.286931),
                                        getLatLng(41.218819, 44.700939)),
            "კახეთი" to listOf(getLatLng(42.550669, 45.309323),
                                getLatLng(42.468606, 45.747069),
                                getLatLng(42.202203, 45.596122),
                                getLatLng(41.909403, 46.401802),
                                getLatLng(41.651919, 46.158400),
                                getLatLng(41.291408, 46.707470),
                                getLatLng(41.076984, 46.479162),
                                getLatLng(41.475449, 45.311210),
                                getLatLng(41.903786, 45.081015)))


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

        fun getLatLng(lat: Double, lng: Double): com.google.android.gms.maps.model.LatLng {
            return com.google.android.gms.maps.model.LatLng(lat, lng)
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