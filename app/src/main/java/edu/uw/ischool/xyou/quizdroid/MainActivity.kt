package edu.uw.ischool.xyou.quizdroid

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.SimpleAdapter
import android.widget.ListView
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val defaultUrl = "http://tednewardsandbox.site44.com/questions.json"
    private val defaultFreq = 10

    private var url: String? = null
    private var frequency: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setup toolbar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.findViewById<TextView>(R.id.title).text = getString(R.string.app_name)
        toolbar.findViewById<ImageView>(R.id.preferences).setOnClickListener {
            handlePreferencesClicked()
        }

        // load preferences
        loadPreferences()

        // load data
        val topics = loadData()

        // render topic list
        renderTopicList(topics)
    }

    private fun handlePreferencesClicked() {
        Log.i(TAG, "Preferences clicked")
        val intent = Intent(this, PreferencesActivity::class.java)
        startActivity(intent)
    }

    private fun loadPreferences() {
        Log.i(TAG, "Loading preferences")
        val sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        url = sharedPref.getString("url", defaultUrl).toString() // default to the given url
        frequency = sharedPref.getInt("frequency", defaultFreq) // default to 0

        val editor = sharedPref.edit()
        if (url == defaultUrl) {
            editor.apply {
                putString("url", defaultUrl)
                apply()
            }
        }
        if (frequency == defaultFreq) {
            editor.apply {
                putInt("frequency", defaultFreq)
                apply()
            }
        }
    }

    private fun loadData(): List<Topic> {
        val app = application as QuizApp

        // do something with the InMemoryTopicRepository

        return app.topicRepository.getTopics()
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