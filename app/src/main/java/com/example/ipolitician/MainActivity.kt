package com.example.ipolitician

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
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
import com.example.ipolitician.structures.User
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val authenticate = Authenticate(this)
    private val PREF_UNIQUE_ID = "PREF_UNIQUE_ID"
    private val DB = DataAPI()

    companion object{
        var uniqueID: String? = null
        var user: User? = null
        private const val TAG = "PhoneAuthActivity"
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
        setTheme(getMode())
        setContentView(R.layout.activity_main)

        retreiveThemeAttrs()

//        authenticate.startPhoneNumberVerification("+995568552663")

        val id = id(context = this)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        navView.itemIconTintList = null
        navView.backgroundTintList = ColorStateList.valueOf(componentColor.data)
        navView.itemTextColor = ColorStateList.valueOf(textColor.data)


        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, args: Bundle? ->
            if (nd.id == R.id.nav_login) {
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
            R.id.action_settings -> loadProfilePopUp(getString(R.string.save))
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

    private fun setUpUser() {
        DB.getUser(uniqueID!!) { user ->
            Log.d("CALLBACK", user.toString())
            if(user != null) {
                Companion.user = user
            } else {
                loadProfilePopUp(getString(R.string.welcome))
            }
        }
    }

    private fun loadProfilePopUp(button_text: String) {
        val inflater = this.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val pw = PopupWindow(
            inflater.inflate(R.layout.fragment_profile, null, false),
            window.decorView.width,
            window.decorView.height - 150,
            true
        )
        pw.animationStyle = R.style.Animation
        val spinner1 = pw.contentView.findViewById<Spinner>(R.id.spinner)
        val spinner2 = pw.contentView.findViewById<Spinner>(R.id.spinner2)
        val spinner3 = pw.contentView.findViewById<Spinner>(R.id.spinner3)
        pw.contentView.findViewById<Button>(R.id.save).text = button_text
        pw.contentView.findViewById<TextView>(R.id.intro).text = getString(R.string.intro)

        val age_idx = if(user != null) user!!.age else 0
        val gender_idx = if(user != null) user!!.gender else 0

        ProfileFragment.setSpinner(
            spinner1,
            context = baseContext,
            ProfileFragment.ages,
            age_idx
        )
        ProfileFragment.setSpinner(
            spinner2,
            context = baseContext,
            ProfileFragment.genders,
            gender_idx
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
            Companion.user = usr
            DB.setUser(uniqueID!!, usr)
            pw.dismiss()
        }
        pw.showAtLocation(findViewById(R.id.nav_view), Gravity.CENTER, 0, 0)
    }

    private fun retreiveThemeAttrs(){
        theme.resolveAttribute(R.attr.textClr, textColor, true)
        theme.resolveAttribute(R.attr.backgroundClr, backgroundColor, true)
        theme.resolveAttribute(R.attr.componentClr, componentColor, true)
    }
}