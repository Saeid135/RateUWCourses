package edu.uw.ischool.saeid135.rateuwcourses

import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController

class FilterProfessor : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
    }

}