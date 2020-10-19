package com.ng.rapetracker.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.ng.rapetracker.R
import com.ng.rapetracker.utils.ClassAlertDialog
import com.ng.rapetracker.utils.ClassSharedPreferences
import com.ng.rapetracker.viewmodel.ModelMainActivity
import com.shuhart.stepview.StepView.OnStepClickListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    lateinit var prefs:ClassSharedPreferences
    lateinit var modelMainActivity: ModelMainActivity
    var firstTimeLaunch = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark2)
        prefs = ClassSharedPreferences(this)
        modelMainActivity = ViewModelProvider(this).get(ModelMainActivity::class.java)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_main_activity)
        NavigationUI.setupActionBarWithNavController(this, navController)

//        if (findNavController().currentDestination?.id == R.id.currentFragment) {
//            findNavController().navigate(R.id.action_current_next)
//        }
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if(destination.id == R.id.fragmentMain||destination.id == R.id.fragmentProfile||destination.id == R.id.fragmentRapeDetail) {
//                supportActionBar?.show()
                hideStepView()
            } else {
//                supportActionBar?.hide()
                when(destination.id){
                    R.id.fragmentLogComplainForm1RapeVictim -> showStepView(0)
                    R.id.fragmentLogComplainForm2TypeOfVictim -> showStepView(1)
                    R.id.fragmentLogComplainForm3TypeOfRape -> showStepView(2)
                    R.id.fragmentLogComplainForm4SelectSupport -> showStepView(3)
                    R.id.fragmentLogComplainForm5RapeDetail -> showStepView(4)
                }
            }

            if (firstTimeLaunch !=0){
                if (destination.id == R.id.fragmentMain){
                    showMenuItems()
                }else{
                    hideMenuItems()
                }
            }
        }
    }
    private fun showStepView(step_number:Int){
        if(step_view.visibility == View.GONE) step_view.visibility = View.VISIBLE

        val steps: MutableList<String> = ArrayList()
        for (i in 0..4) {
            steps.add("${i + 1}")
        }

        step_view.setSteps(steps)
        step_view.go(step_number,true)
//        step_view.done(true)
        step_view.setOnStepClickListener(OnStepClickListener { step ->
//            Toast.makeText(this,"Step $step",Toast.LENGTH_SHORT).show()
        })
    }
    private fun hideStepView(){
        step_view.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            navController,
            null
        )
    }


    private var log_complain: MenuItem? = null
    private var log_complain2: MenuItem? = null
    private var profile: MenuItem? = null
    private var logout: MenuItem? = null
    private fun showMenuItems(){
        if(prefs.getAccessLevel()==1){
            log_complain!!.isVisible = true
            log_complain2!!.isVisible = true
        }
        profile!!.isVisible = true
        logout!!.isVisible = true
    }
    private fun hideMenuItems(){
        log_complain!!.isVisible = false
        log_complain2!!.isVisible = false
        profile!!.isVisible = false
        logout!!.isVisible = false
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        log_complain = menu.findItem(R.id.log_complain)
        log_complain2 = menu.findItem(R.id.log_complain2)
        profile = menu.findItem(R.id.profile)
        logout = menu.findItem(R.id.logout)

        if(prefs.getAccessLevel()==2){
            menu.findItem(R.id.log_complain).isVisible = false
            menu.findItem(R.id.log_complain2).isVisible = false
        }
        firstTimeLaunch++
        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.log_complain -> {
//                findNavController(R.id.nav_host_fragment_main_activity).navigate(FragmentMainDirections.actionFragmentMainToFragmentLogComplainForm1RapeVictim())
                if(prefs.getAccessLevel()==2){
                    ClassAlertDialog(this).alertMessage("You cannot Log Rape Complain because you are a Support Organization")
                    return true
                }
                modelMainActivity.setGotoFormFrag("form")
            }
            R.id.log_complain2 -> {
                if(prefs.getAccessLevel()==2){
                    ClassAlertDialog(this).alertMessage("You cannot Log Rape Complain because you are a Support Organization")
                    return true
                }
                modelMainActivity.setGotoFormFrag("form")
            }
            R.id.profile ->{
                navController.navigate(R.id.action_fragmentMain_to_fragmentProfile)
            }
            R.id.refresh_list ->{
                modelMainActivity.setGotoFormFrag("refresh")
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