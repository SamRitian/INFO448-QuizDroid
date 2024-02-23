package edu.uw.ischool.xyou.quizdroid

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.ImageView
import android.widget.SimpleAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private var url = "https://tednewardsandbox.site44.com/questions.json"
    private var frequency = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "Creating MainActivity")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setup toolbar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.findViewById<TextView>(R.id.title).text = getString(R.string.app_name)
        toolbar.findViewById<ImageView>(R.id.preferences).setOnClickListener {
            handlePreferencesClicked()
        }
        toolbar.findViewById<ImageView>(R.id.refresh).setOnClickListener {
            refreshListView()
        }

        // check connectivity
        checkConnectivity()

        // load preferences
        loadPreferences()

        // download data
        startBackgroundService()
    }

    override fun onResume() {
        Log.i(TAG, "Resuming MainActivity")
        super.onResume()

        // check connectivity
        checkConnectivity()

        // load preferences
        loadPreferences()

        // load data
        val topics = loadData()

        // render topic list
        renderTopicList(topics)
    }

    private fun refreshListView() {
        val topics = loadData()
        renderTopicList(topics)
    }

    private fun checkConnectivity() {
        if (!isOnline(this)) {
            if (isAirplaneModeOn(this)) {
                AlertDialog.Builder(this)
                    .setTitle("No Internet Connection")
                    .setMessage("Airplane mode is on. Would you like to turn it off?")
                    .setPositiveButton("Yes") { _, _ ->
                        goToSettings(this)
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
        }
    }

    private fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    private fun isAirplaneModeOn(context: Context): Boolean {
        return Settings.System.getInt(
            context.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON,
            0
        ) != 0
    }

    private fun goToSettings(context: Context) {
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
        startActivity(intent)
    }

    private fun handlePreferencesClicked() {
        val intent = Intent(this, PreferencesActivity::class.java)
        startActivity(intent)
    }

    private fun loadPreferences() {
        val sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        url = sharedPref.getString("url", url).toString() // default to the given url
        frequency = sharedPref.getInt("frequency", frequency) // default to 0

        val editor = sharedPref.edit()
        editor.apply {
            putString("url", url)
            putInt("frequency", frequency)
            apply()
        }
    }

    private fun startBackgroundService() {
        val intent = Intent(this, DownloadService::class.java).apply {
            putExtra("url", url)
            putExtra("frequency", frequency)
        }
        startService(intent)
    }

    private fun loadData(): List<Topic> {
        // get data from the repository
        val repository = InMemoryTopicRepository(this)
        return repository.getTopics()
    }

    private fun renderTopicList(topics: List<Topic>) {
        val listView = findViewById<ListView>(R.id.topic_list)

        val data = ArrayList<HashMap<String, String>>()
        for (i in topics.indices) {
            val map = HashMap<String, String>()
            map["name"] = topics[i].name
            map["shortDescr"] = topics[i].shortDescription
            data.add(map)
        }

        val from = arrayOf("name", "shortDescr")
        val to = intArrayOf(R.id.topic_name, R.id.topic_short_description)

        val adapter = SimpleAdapter(this, data, R.layout.quiz_title, from, to)
        listView.adapter = adapter

        // when click on the topic, go to the topic overview page
        listView.setOnItemClickListener { _, _, position, _ ->
            val topic = topics[position]
            val intent = Intent(this, QuizActivity::class.java).apply {
                putExtra("topicName", topic.name)
            }
            startActivity(intent)
        }
    }
}