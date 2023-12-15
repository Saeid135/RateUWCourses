package edu.uw.ischool.saeid135.rateuwcourses

import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.telephony.SmsManager
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.util.Log
import android.view.View
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController

class FilterProfessor : AppCompatActivity() {
    lateinit var enterVal : EditText
    lateinit var enterBtn : Button
    lateinit var enterText : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filter_privacy)
        enterVal = findViewById(R.id.enterVal)
        enterBtn = findViewById(R.id.enterBtn)
        enterText = findViewById(R.id.enterText)
        enterVal.visibility = View.VISIBLE
        enterBtn.visibility = View.VISIBLE
        enterBtn.setOnClickListener{
            val smsManager: SmsManager = SmsManager.getDefault()
            smsManager.sendTextMessage("+12063312285", null,
                enterVal.text.toString(), null, null)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

}