package edu.uw.ischool.xyou.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import modal.Topic
import modal.topics

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupTopicList(topics)
    }

    private fun setupTopicList(topics: List<Topic>) {
        // Convert the List of Topics to an Array of topic names for the ArrayAdapter
        val topicNames = topics.map { it.name }.toTypedArray()
        val topicList = findViewById<ListView>(R.id.topic_list)
        val adapter = ArrayAdapter(this, R.layout.quiz_topic, R.id.topic_item, topicNames)
        topicList.adapter = adapter

        topicList.setOnItemClickListener { adapterView, view, i, l ->
            val topic = topics[i]
            val intent = Intent(this, TopicOverviewActivity::class.java).apply {
                putExtra("TOPIC_NAME", topic.name)
                putExtra("TOPIC_DESCRIPTION", topic.description)
                putExtra("QUESTION_COUNT", topic.questions.size)
            }
            startActivity(intent)
        }
    }
}