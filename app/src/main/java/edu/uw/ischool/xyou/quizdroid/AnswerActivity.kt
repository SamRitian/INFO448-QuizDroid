package edu.uw.ischool.xyou.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import modal.topics

class AnswerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer)

        // Retrieve passed values
        val topicName = intent.getStringExtra("TOPIC_NAME")
        val selectedAnswerIndex = intent.getIntExtra("SELECTED_ANSWER", -1)
        val correctAnswerIndex = intent.getIntExtra("CORRECT_ANSWER", -1)
        val currentScore = intent.getIntExtra("CURRENT_SCORE", 0)
        val questionIndex = intent.getIntExtra("QUESTION_INDEX", 0)
        val totalQuestions = intent.getIntExtra("TOTAL_QUESTIONS", 0)

        // Assuming questions are passed or accessible globally like before
        val question = topics.find { it.name == topicName }?.questions?.get(questionIndex)

        // Update UI elements
        val selectedAnswerText = findViewById<TextView>(R.id.selected_answer_text)
        val correctAnswerText = findViewById<TextView>(R.id.correct_answer_text)
        val scoreText = findViewById<TextView>(R.id.score_text)

        selectedAnswerText.text = "Your answer: ${question?.options?.get(selectedAnswerIndex)}"
        correctAnswerText.text = "Correct answer: ${question?.options?.get(correctAnswerIndex)}"

        val updatedScore = if (selectedAnswerIndex == correctAnswerIndex) currentScore + 1 else currentScore
        scoreText.text = "Score: $updatedScore out of $totalQuestions"

        // Configure Next/Finish Button
        val nextButton = findViewById<Button>(R.id.next_button)
        if (questionIndex + 1 < totalQuestions) {
            nextButton.text = "Next"
            nextButton.setOnClickListener {
                // Start QuestionActivity for the next question
                val intent = Intent(this, QuestionActivity::class.java).apply {
                    putExtra("TOPIC_NAME", topicName)
                    putExtra("CURRENT_SCORE", updatedScore)
                    putExtra("QUESTION_INDEX", questionIndex + 1)
                    putExtra("TOTAL_QUESTIONS", totalQuestions)
                }
                startActivity(intent)
            }
        } else {
            nextButton.text = "Finish"
            nextButton.setOnClickListener {
                // Navigate back to the topics list or show a summary
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}