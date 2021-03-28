package com.example.ipolitician

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ipolitician.structures.Selected
import com.example.ipolitician.structures.User
import com.example.ipolitician.nav.profile.ProfileFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val PREF_UNIQUE_ID = "PREF_UNIQUE_ID"
    private val FS = Firebase.firestore

    companion object{
        var uniqueID: String? = null
        var user: User? = null
    }

    @Synchronized
    fun id(context: Context): String? {
        if (uniqueID == null) {
            val sharedPrefs: SharedPreferences = context.getSharedPreferences(
                PREF_UNIQUE_ID, Context.MODE_PRIVATE
            )
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null)
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString()
                val editor: SharedPreferences.Editor = sharedPrefs.edit()
                editor.putString(PREF_UNIQUE_ID, uniqueID)
                editor.apply()
            }
        }
        return uniqueID
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val id = id(context = this)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        navView.itemIconTintList = null
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_public,
                R.id.nav_survey,
                R.id.nav_profile,
                R.id.nav_problems
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        setUpUser()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun setUpUser() {
        uniqueID?.let {
            FS.collection("users").document(it)
                .get()
                .addOnSuccessListener { usr ->
                    if (usr.exists()) {
                        user = usr.toObject(User::class.java)
                    } else {
                        val inflater =
                            this.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                        val pw = PopupWindow(
                            inflater.inflate(R.layout.fragment_profile, null, false),
                            window.decorView.width,
                            window.decorView.height - 200,
                            true
                        )
                        val spinner1 = pw.contentView.findViewById<Spinner>(R.id.spinner)
                        val spinner2 = pw.contentView.findViewById<Spinner>(R.id.spinner2)
                        val spinner3 = pw.contentView.findViewById<Spinner>(R.id.spinner3)
                        pw.contentView.findViewById<Button>(R.id.save).text = getString(R.string.welcome)
                        pw.contentView.findViewById<TextView>(R.id.intro).text = getString(R.string.intro)
                        ProfileFragment.setSpinner(
                            spinner1,
                            context = baseContext,
                            ProfileFragment.ages
                        )
                        ProfileFragment.setSpinner(
                            spinner2,
                            context = baseContext,
                            ProfileFragment.genders
                        )
                        ProfileFragment.setSpinner(
                            spinner3,
                            context = baseContext,
                            ProfileFragment.regions
                        )
                        pw.contentView.findViewById<Button>(R.id.save).setOnClickListener {
                            val usr = User(
                                age = spinner1.selectedItemPosition,
                                gender = spinner2.selectedItemPosition
                            )
                            user = usr
                            FS.collection("users").document(uniqueID!!)
                                .set(usr)
                                .addOnSuccessListener { Log.d("listener", "yep") }
                                .addOnFailureListener { Log.d("listener", "nope") }
                            FS.collection("submissions").document(uniqueID!!)
                                .set(Selected(selected = arrayListOf(-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)))
                                .addOnSuccessListener { Log.d("listener", "yep") }
                                .addOnFailureListener { Log.d("listener", "nope") }
                            pw.dismiss()
                        }
                        pw.showAtLocation(findViewById(R.id.home), Gravity.CENTER, 0, 0)
                    }
                }
        }
    }


}