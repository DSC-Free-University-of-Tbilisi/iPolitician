package com.example.ipolitician

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.anychart.ui.contextmenu.Item
import com.example.ipolitician.Auth.Authenticate
import com.example.ipolitician.firebase.DataAPI
import com.example.ipolitician.nav.profile.ProfileFragment
import com.example.ipolitician.structures.EV
import com.example.ipolitician.structures.QA
import com.example.ipolitician.structures.User
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val PREF_UNIQUE_ID = "PREF_UNIQUE_ID"
    private val DB = DataAPI()

    companion object{
        var uniqueID: String? = null
        var user: User? = null
        private const val TAG = "PhoneAuthActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(getMode())
        setContentView(R.layout.activity_main)
        retreiveThemeAttrs()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        navView.itemIconTintList = null
        navView.backgroundTintList = ColorStateList.valueOf(componentColor.data)
        navView.itemTextColor = ColorStateList.valueOf(textColor.data)


        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, args: Bundle? ->
            if (nd.id == R.id.nav_login || nd.id == R.id.nav_profile) {
//                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                supportActionBar?.hide()
            } else {
                supportActionBar?.show()
//                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
        }
//        navController.navigate(R.id.nav_login)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_public,
                R.id.nav_survey,
                R.id.nav_problems,
                R.id.nav_vocab,
                R.id.nav_election
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

//        setUpUser()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        val switch = findViewById<View>(R.id.switch_theme) as SwitchCompat
        switch.isChecked = getMode() == R.style.DarkMode_NoActionBar
        switch.setOnClickListener(View.OnClickListener {
            Toast.makeText(
                applicationContext,
                if (switch.isChecked) "Dark Mode" else "Light Mode",
                Toast.LENGTH_SHORT
            ).show()
            switchMode()
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> findNavController(R.id.nav_host_fragment).navigate(R.id.nav_profile)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = authenticate.auth.currentUser
//        updateUI(currentUser)
    }

//    private fun updateUI(user: FirebaseUser? = authenticate.auth.currentUser) {
//
//    }

//    private fun setUpUser() {
//        DB.getUser(uniqueID!!) { user ->
//            Log.d("CALLBACK", user.toString())
//            if(user != null) {
//                Companion.user = user
//            } else {
////                loadProfilePopUp(getString(R.string.welcome))
//            }
//        }
//    }

    private fun retreiveThemeAttrs(){
        theme.resolveAttribute(R.attr.textClr, textColor, true)
        theme.resolveAttribute(R.attr.backgroundClr, backgroundColor, true)
        theme.resolveAttribute(R.attr.componentClr, componentColor, true)
    }
}