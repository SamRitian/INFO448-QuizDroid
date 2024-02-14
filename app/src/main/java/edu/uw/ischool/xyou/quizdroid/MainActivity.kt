package edu.uw.ischool.xyou.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SimpleAdapter
import android.widget.ListView

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setup toolbar
        setSupportActionBar(findViewById(R.id.toolbar))

        val app = application as QuizApp
        val topics = app.topicRepository.getTopics()

        renderTopicList(topics)
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