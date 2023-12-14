package edu.uw.ischool.saeid135.rateuwcourses

import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
import android.app.AlarmManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.app.Application
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import androidx.activity.ComponentActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import edu.uw.ischool.saeid135.rateuwcourses.ui.theme.RateUWCoursesTheme

data class Courses(
    val name: String,
    val description: String,
    val professor: String,
    val rating: Float
)

interface courseRepository {
    fun getAll(): List<Courses>
}

class CourseRepoValues(val context: Context) : courseRepository {
    val type = object : TypeToken<List<Courses>>() {}.type
    val gson = Gson()
    val json: String =
        context.resources.openRawResource(R.raw.coursedata).bufferedReader().use { it.readText() }
    var coursesList: List<Courses> = gson.fromJson(json, type)


    override fun getAll(): List<Courses> {
        return coursesList
    }
}

class CourseApplication : Application() {
    lateinit var ratingRepository: courseRepository
    var fileRepository: List<Courses> = listOf()

    override fun onCreate() {
        super.onCreate()
        Log.i("CourseApp", "onCreate()")
        val file = File(getExternalFilesDir(null).toString() + "/coursedata.json")
        Log.i("This is file path", file.toString())
        if (file.exists()) {
            val gson = Gson()
            val type = object : TypeToken<List<Courses>>() {}.type
            val json = file.bufferedReader().use { it.readText() }
            fileRepository = gson.fromJson(json, type)
            Log.i("This file is Here", "Here I am!")
        } else {
            ratingRepository = CourseRepoValues(this)
            Log.i("Nope!", "It isn't here")

        }
    }
}


class MainActivity : AppCompatActivity() {
    lateinit var listView: ListView
    var allCourses: List<Courses> = mutableListOf()
    lateinit var searchView: SearchView
    lateinit var profBtn: Button
    lateinit var newCourses: List<String>
    lateinit var profOrCourse: String
    lateinit var appTitle : TextView
    lateinit var appBtn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        downloadFile()
        val file = File(getExternalFilesDir(null).toString() + "/coursedata.json")
        val fullRating = (application as CourseApplication)
        if (file.exists()) {
            if (fullRating.fileRepository.isEmpty()){
                val gson = Gson()
                val type = object : TypeToken<List<Courses>>() {}.type
                val json = file.bufferedReader().use { it.readText() }
                allCourses = gson.fromJson(json, type)
            }
            else {
                allCourses = fullRating.fileRepository
            }
            Log.i("I exist", allCourses.toString())
        } else if (!file.exists()) {
            val repository = fullRating.ratingRepository
            allCourses = repository.getAll()
            Log.i("I don't exist", allCourses.toString())
        }

        profOrCourse = "Course"
        listView = findViewById(R.id.listView)
        searchView = findViewById(R.id.searchView)
        profBtn = findViewById(R.id.profBtn)
        appTitle = findViewById(R.id.appTitle)
        appBtn = findViewById(R.id.appBtn)
        val filePath = this.filesDir.absolutePath
        Log.i("Check files", filePath)
        newCourses = allCourses.map { it.name }
        appBtn.setOnClickListener {
            listView.visibility = View.VISIBLE
            searchView.visibility = View.VISIBLE
            profBtn.visibility = View.VISIBLE
            appTitle.visibility = View.GONE
            appBtn.visibility = View.GONE

        }
        profBtn.setOnClickListener {
            if (profOrCourse == "Course") {
                newCourses = allCourses.map { it.professor }
                val adapter = ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, newCourses
                )
                listView.adapter = adapter
                profOrCourse = "Professor"
                profBtn.text = "Courses"
                searchView.queryHint = "Search for a Professor"
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (newCourses.contains(query)) {
                            adapter.filter.filter(query)
                        } else {
                            Toast.makeText(this@MainActivity, "No Classes Found", Toast.LENGTH_LONG)
                                .show()
                        }
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        adapter.filter.filter(newText)
                        return false
                    }
                })
            } else {
                newCourses = allCourses.map { it.name }
                val adapter = ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, newCourses
                )
                listView.adapter = adapter
                profOrCourse = "Course"
                profBtn.text = "Professors"
                searchView.queryHint = "Search for a Course"
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (newCourses.contains(query)) {
                            adapter.filter.filter(query)
                        } else {
                            Toast.makeText(this@MainActivity, "No Classes Found", Toast.LENGTH_LONG)
                                .show()
                        }
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        adapter.filter.filter(newText)
                        return false
                    }
                })
            }
        }
        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1, android.R.id.text1, newCourses
        )
        listView.adapter = adapter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (newCourses.contains(query)) {
                    adapter.filter.filter(query)
                } else {
                    Toast.makeText(this@MainActivity, "No Classes Found", Toast.LENGTH_LONG)
                        .show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val intent = Intent(this, DetailActivity::class.java)
                val selectedItem = parent.getItemAtPosition(position) as String
                intent.putExtra("chosenCourse", selectedItem)
                val gson = Gson()
                val json = gson.toJson(allCourses)
                intent.putExtra("coursesList", json)

                Log.i("Check error", selectedItem)
                startActivity(intent)
            }
    }

    fun downloadFile() {
        val activityThis = this
        val file = File(getExternalFilesDir(null).toString() + "/coursedata.json")
        if (!file.exists()) {
            Log.i("File Downloaded", "File Downloaded")
            val executor: Executor = Executors.newSingleThreadExecutor()
            executor.execute {
                val url = URL("https://api.jsonbin.io/v3/b/657b26f1266cfc3fde68cd07?meta=false")
                val urlConnection = url.openConnection() as HttpURLConnection
                val inputStream = urlConnection.getInputStream()
                val reader = InputStreamReader(inputStream)
                reader.use {
                    val text = it.readText()
                    activityThis.runOnUiThread {
                        File(getExternalFilesDir(null).toString() + "/coursedata.json").writeText(
                            text
                        )
                    }
                }
            }
            val intent = Intent(activityThis, MainActivity::class.java)
            startActivity(intent)
        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.report -> {
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}


