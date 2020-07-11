package com.ng.rapetracker.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.ng.rapetracker.R
import com.ng.rapetracker.ui.fragment.act_main.FragmentMainDirections
import com.ng.rapetracker.utils.ClassSharedPreferences

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    lateinit var prefs:ClassSharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark2)
        prefs = ClassSharedPreferences(this)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_main_activity)
        NavigationUI.setupActionBarWithNavController(this, navController)


    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            navController,
            null
        )
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.log_complain -> {
//                findNavController(R.id.nav_host_fragment_main_activity).navigate(FragmentMainDirections.actionFragmentMainToFragmentLogComplainForm1RapeVictim())
            }
            R.id.refresh_app -> {

            }
            R.id.profile ->{

            }
            R.id.logout->{
                prefs.logoutUser()
                startActivity(Intent(this,   ActivityLogin::class.java))
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

}