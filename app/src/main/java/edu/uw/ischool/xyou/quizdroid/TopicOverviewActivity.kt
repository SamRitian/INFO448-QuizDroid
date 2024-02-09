package edu.uw.ischool.xyou.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class TopicOverviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic_overview)

        val topicName = intent.getStringExtra("TOPIC_NAME")
        val description = intent.getStringExtra("TOPIC_DESCRIPTION")
        val questionCount = intent.getIntExtra("QUESTION_COUNT", 0)

        findViewById<TextView>(R.id.topic_description).text = description
        findViewById<TextView>(R.id.question_count).text = "Total Questions: $questionCount"

        // Setup the Begin button
        findViewById<Button>(R.id.begin_button).setOnClickListener {
            val intent = Intent(this, QuestionActivity::class.java).apply {
                putExtra("TOPIC_NAME", topicName)
            }
            startActivity(intent)
        }
    }
}