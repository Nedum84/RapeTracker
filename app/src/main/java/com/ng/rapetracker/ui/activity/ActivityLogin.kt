package com.ng.rapetracker.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.happinesstonic.viewmodel.ModelLoginActivity
import com.ng.rapetracker.R
import com.ng.rapetracker.databinding.ActivityLoginBinding


class ActivityLogin : AppCompatActivity() {
    private lateinit var dataBinding:ActivityLoginBinding
    lateinit var viewModelLoginActivity: ModelLoginActivity



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        viewModelLoginActivity = ViewModelProvider(this).get(ModelLoginActivity::class.java)

        viewModelLoginActivity.gotoMainActivity.observe(this, Observer {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        })
    }


}