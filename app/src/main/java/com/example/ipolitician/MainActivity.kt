package com.example.ipolitician

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ipolitician.firebase.DataAPI
import com.example.ipolitician.structures.User
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.richpath.RichPath
import com.richpath.RichPathView
import com.richpathanimator.RichPathAnimator


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

        val tribune: MenuItem = navView.menu.findItem(R.id.tribune)
        val ts = SpannableString(tribune.title)
        ts.setSpan(TextAppearanceSpan(this, R.style.SwitchCompatTheme), 0, ts.length, 0)
        tribune.title = ts

        val options: MenuItem = navView.menu.findItem(R.id.options)
        val os = SpannableString(options.title)
        os.setSpan(TextAppearanceSpan(this, R.style.SwitchCompatTheme), 0, os.length, 0)
        options.title = os

        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, args: Bundle? ->
            if (nd.id == R.id.nav_login || nd.id == R.id.nav_profile) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                supportActionBar?.hide()
            } else {
                supportActionBar?.show()
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
        }

        drawerLayout.addDrawerListener(object: DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerOpened(drawerView: View) {
                animateGeorgia(user!!.region)
            }

            override fun onDrawerClosed(drawerView: View) {}

            override fun onDrawerStateChanged(newState: Int) {}

        })
//        navController.navigate(R.id.nav_login)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_public,
                R.id.nav_survey,
                R.id.nav_cesko,
                R.id.nav_problems,
                R.id.nav_vocab,
                R.id.nav_election
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        setUpNavHeader(navView.getHeaderView(0))
//        navView.setNavigationItemSelectedListener(this)
    }

    private fun setUpNavHeader(navHeader: View) {
        val notificationsRichPathView = navHeader.findViewById<RichPathView>(R.id.app_nav_header_background)

        notificationsRichPathView.setOnPathClickListener(RichPath.OnPathClickListener { richPath ->
            Snackbar.make(navHeader, richPath.name, Snackbar.LENGTH_LONG).setAction(
                "Action",
                null
            ).show()
        })
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        val switch = findViewById<View>(R.id.switch_theme) as SwitchCompat
        switch.isChecked = getMode() == R.style.DarkMode_NoActionBar
//        switch.setOnClickListener(View.OnClickListener {
//            Toast.makeText(
//                applicationContext,
//                if (switch.isChecked) "Dark Mode" else "Light Mode",
//                Toast.LENGTH_SHORT
//            ).show()
//            switchMode()
//        })
        switch.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(
                applicationContext,
                if (isChecked) "Dark Mode" else "Light Mode",
                Toast.LENGTH_SHORT
            ).show()
            switchMode()
            animateGeorgia(user!!.region)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> findNavController(R.id.nav_host_fragment).navigate(R.id.nav_profile)
            R.id.action_log_out -> findNavController(R.id.nav_host_fragment).navigate(R.id.nav_login)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun retreiveThemeAttrs(){
        theme.resolveAttribute(R.attr.textClr, textColor, true)
        theme.resolveAttribute(R.attr.backgroundClr, backgroundColor, true)
        theme.resolveAttribute(R.attr.componentClr, componentColor, true)
        theme.resolveAttribute(R.attr.buttonColorStart, buttonColor, true)
    }

    fun animateGeorgia(region: String): Boolean {
        val navHeader = findViewById<NavigationView>(R.id.nav_view).getHeaderView(0)
        navHeader.setBackgroundResource(R.drawable.button_background_shape)

        val notificationsRichPathView = navHeader.findViewById<RichPathView>(R.id.app_nav_header_background)
        notificationsRichPathView.setVectorDrawable(R.drawable.ic_georgia)

        for(i in 0..11) {
            val path = notificationsRichPathView.findRichPathByIndex(i)
            if(region == path!!.name) {
                RichPathAnimator.animate(path)
                    .interpolator(DecelerateInterpolator())
                    .fillColor(Color.GREEN)
                    .duration(8000)
                    .start()
            }

            RichPathAnimator.animate(path)
                .interpolator(DecelerateInterpolator())
                .strokeColor(buttonColor.data)
                .duration(8000)
                .start()
        }
        return true
    }
}