package edu.uw.ischool.saeid135.rateuwcourses

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.activity.ComponentActivity
import android.widget.Button
import android.content.Intent
import android.graphics.Color
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import edu.uw.ischool.saeid135.rateuwcourses.databinding.ActivityDetailBinding
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.File
import java.io.InputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class DetailActivity : AppCompatActivity() {
    private lateinit var txtDesc : TextView
    private lateinit var txtName : TextView
    private lateinit var txtProf : TextView
    private lateinit var txtRating : TextView
    private lateinit var titleDesc : TextView
    private lateinit var titleProf : TextView
    private lateinit var titleRating : TextView
    private lateinit var positiveBtn : Button
    private lateinit var negativeBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val courseDesc = intent.getStringExtra("chosenCourse")
        val json = intent.getStringExtra("coursesList")
        val gson = Gson()
        val type = object : TypeToken<List<Courses>>() {}.type
        val coursesList : List<Courses> = gson.fromJson(json, type)
        val totalCount = coursesList.count()
        titleDesc = findViewById(R.id.titleDesc)
        titleProf = findViewById(R.id.titleProf)
        titleRating = findViewById(R.id.titleRating)
        txtName = findViewById(R.id.txtName)
        txtDesc = findViewById(R.id.txtDesc)
        txtProf = findViewById(R.id.txtProf)
        txtRating = findViewById(R.id.txtRating)
        positiveBtn = findViewById(R.id.positiveBtn)
        negativeBtn = findViewById(R.id.negativeBtn)
        var keepTrack = 0
        for (x in coursesList) {
            if (x.name == courseDesc) {
                break
            }
            else if (x.professor == courseDesc) {
                break
            }
            keepTrack += 1
        }
        txtName.text = coursesList[keepTrack].name
        txtDesc.text = coursesList[keepTrack].description
        txtProf.text = coursesList[keepTrack].professor
        txtRating.text = coursesList[keepTrack].rating.toString()
        positiveBtn.setOnClickListener {
            if (positiveBtn.tag == "Unchanged" && negativeBtn.tag == "Unchanged") {
                Log.i("Changed1", "Changed")
                positiveBtn.setBackgroundColor(Color.parseColor("#00FF00"))
                positiveBtn.tag = "Changed"
                coursesList[keepTrack].rating = (coursesList[keepTrack].rating + 0.01).toFloat()
                val newjson = gson.toJson(coursesList)
                File(getExternalFilesDir(null).toString() + "/coursedata.json").writeText(
                    newjson
                )
            }
            else if (negativeBtn.tag == "Changed" && positiveBtn.tag == "Unchanged") {
                Log.i("Changed2", "Changed")
                positiveBtn.setBackgroundColor(Color.parseColor("#00FF00"))
                negativeBtn.setBackgroundColor(Color.parseColor("#FFFFFF"))
                negativeBtn.tag = "Unchanged"
                positiveBtn.tag = "Changed"
                coursesList[keepTrack].rating = (coursesList[keepTrack].rating + 0.01).toFloat()
                val newjson = gson.toJson(coursesList)
                File(getExternalFilesDir(null).toString() + "/coursedata.json").writeText(
                    newjson
                )
            }
            else {
                Log.i("Changed3", "Changed")
                positiveBtn.setBackgroundColor(Color.parseColor("#FFFFFF"))
                positiveBtn.tag = "Unchanged"
                coursesList[keepTrack].rating = (coursesList[keepTrack].rating - 0.01).toFloat()
                val newjson = gson.toJson(coursesList)
                File(getExternalFilesDir(null).toString() + "/coursedata.json").writeText(
                    newjson
                )
            }
        }
        negativeBtn.setOnClickListener {
            if (negativeBtn.tag == "Unchanged" && positiveBtn.tag == "Unchanged") {
                negativeBtn.setBackgroundColor(Color.parseColor("#FF0000"))
                negativeBtn.tag = "Changed"
                coursesList[keepTrack].rating = (coursesList[keepTrack].rating - 0.01).toFloat()
                val newjson = gson.toJson(coursesList)
                File(getExternalFilesDir(null).toString() + "/coursedata.json").writeText(
                    newjson
                )
            }
            else if (positiveBtn.tag == "Changed" && negativeBtn.tag == "Unchanged") {
                negativeBtn.setBackgroundColor(Color.parseColor("#FF0000"))
                positiveBtn.setBackgroundColor(Color.parseColor("#FFFFFF"))
                positiveBtn.tag = "Unchanged"
                negativeBtn.tag = "Changed"
                coursesList[keepTrack].rating = (coursesList[keepTrack].rating - 0.01).toFloat()
                val newjson = gson.toJson(coursesList)
                File(getExternalFilesDir(null).toString() + "/coursedata.json").writeText(
                    newjson
                )
            }
            else {
                negativeBtn.setBackgroundColor(Color.parseColor("#FFFFFF"))
                negativeBtn.tag = "Unchanged"
                coursesList[keepTrack].rating = (coursesList[keepTrack].rating + 0.01).toFloat()
                val newjson = gson.toJson(coursesList)
                File(getExternalFilesDir(null).toString() + "/coursedata.json").writeText(
                    newjson
                )
            }
        }

    }
}